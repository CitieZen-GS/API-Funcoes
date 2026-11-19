#
# VERIFIQUE SE ESTA COM A ASSINATURA CORRRETA
#
# az account show
# az account list -o table
# az account set --subscription "nome-da-assinatura-correta"


# Variáveis
# Este script cria a infraestrutura compartilhada (grupo, Application Insights, plano B1 e RabbitMQ)
# e o Web App da API de funções. As outras APIs usam o mesmo grupo e plano: rode este script primeiro.
RESOURCE_GROUP_NAME="gs-ajudantefamiliar"
WEB_APP_NAME="ajudante-familiar-funcoes"   # igual ao WEB_APP_NAME do azure-pipeline.yaml
APP_SERVICE_PLAN="ajudante-familiar-SP"
LOCATION="brazilsouth"
RUNTIME="JAVA:21-java21"
APP_INSIGHTS_NAME="ai-ajudante-familiar"

# RabbitMQ (Azure Container Instance), usado pela mensageria entre as APIs
RABBITMQ_CONTAINER_NAME="rabbitmq-ajudante-familiar"
RABBITMQ_DNS_LABEL="rabbitmq-ajudante-familiar"   # vira <label>.brazilsouth.azurecontainer.io; precisa ser único na região
RABBITMQ_IMAGE="rabbitmq:4-management"            # inclui o painel web de administração (porta 15672)
RABBITMQ_USER="ajudante"

# Senha do RabbitMQ: digitada aqui, não aparece na tela e não fica salva neste arquivo.
# Guarde-a: ela vai na variável secreta SPRING_RABBITMQ_PASSWORD das pipelines.
read -s -p "Defina a senha do RabbitMQ (mínimo 12 caracteres): " RABBITMQ_PASSWORD
echo
if [ ${#RABBITMQ_PASSWORD} -lt 12 ]; then
  echo "Senha muito curta. A porta do RabbitMQ fica aberta para a internet: use 12 caracteres ou mais."
  exit 1
fi

# Registrar os Serviços utilizados
az provider register --namespace Microsoft.Web
az provider register --namespace Microsoft.Insights
az provider register --namespace Microsoft.OperationalInsights
az provider register --namespace Microsoft.ServiceLinker
az provider register --namespace Microsoft.ContainerInstance

# Registrar a extensão utilizada
az extension add --name application-insights

# Criação do Grupo de Recursos
az group create \
  --name $RESOURCE_GROUP_NAME \
  --location "$LOCATION"
  
# Criar Application Insights
az monitor app-insights component create \
  --app $APP_INSIGHTS_NAME \
  --location "$LOCATION" \
  --resource-group $RESOURCE_GROUP_NAME \
  --application-type web


# Criar o Plano de Serviço
az appservice plan create \
  --name $APP_SERVICE_PLAN \
  --resource-group $RESOURCE_GROUP_NAME \
  --location "$LOCATION" \
  --sku B1 \
  --is-linux


# Criar o Serviço de Aplicativo
az webapp create \
  --name $WEB_APP_NAME \
  --resource-group $RESOURCE_GROUP_NAME \
  --plan $APP_SERVICE_PLAN \
  --runtime "$RUNTIME"

# Recuperar a String de Conexão do Application Insights (assim podemos nos conectar a esse App Insights)
CONNECTION_STRING=$(az monitor app-insights component show \
  --app $APP_INSIGHTS_NAME \
  --resource-group $RESOURCE_GROUP_NAME \
  --query connectionString \
  --output tsv)

# Configurar as Variáveis de Ambiente necessárias do nosso App e do Application Insights
az webapp config appsettings set \
  --name "$WEB_APP_NAME" \
  --resource-group "$RESOURCE_GROUP_NAME" \
  --settings \
    APPLICATIONINSIGHTS_CONNECTION_STRING="$CONNECTION_STRING" \
    ApplicationInsightsAgent_EXTENSION_VERSION="~3" \
    XDT_MicrosoftApplicationInsights_Mode="Recommended" \
    XDT_MicrosoftApplicationInsights_PreemptSdk="1"
# Banco, RabbitMQ e JWT_SEGREDO NÃO ficam aqui: a pipeline (azure-pipeline.yaml) grava essas
# variáveis no Web App a cada deploy, a partir das Variables do Azure DevOps (senhas como secretas).

# Manter o app sempre ligado (disponível no B1): evita a demora de subir o Spring Boot a cada acesso
az webapp config set \
  --name "$WEB_APP_NAME" \
  --resource-group "$RESOURCE_GROUP_NAME" \
  --always-on true

# Reiniciar o Web App
az webapp restart --name $WEB_APP_NAME --resource-group $RESOURCE_GROUP_NAME

# Criar a conexão do nosso Web App com o Application Insights
az monitor app-insights component connect-webapp \
    --app $APP_INSIGHTS_NAME \
    --web-app $WEB_APP_NAME \
    --resource-group $RESOURCE_GROUP_NAME

# Criar o RabbitMQ no Azure Container Instance
# 5672: porta AMQP usada pelas APIs. 15672: painel web (http://<host>:15672), mesmo usuário e senha.
# Sem volume: filas e mensagens se perdem se o container for recriado (suficiente para o projeto).
az container create \
  --name $RABBITMQ_CONTAINER_NAME \
  --resource-group $RESOURCE_GROUP_NAME \
  --location "$LOCATION" \
  --image $RABBITMQ_IMAGE \
  --os-type Linux \
  --cpu 1 \
  --memory 1.5 \
  --ip-address Public \
  --dns-name-label $RABBITMQ_DNS_LABEL \
  --ports 5672 15672 \
  --restart-policy Always \
  --environment-variables RABBITMQ_DEFAULT_USER="$RABBITMQ_USER" \
  --secure-environment-variables RABBITMQ_DEFAULT_PASS="$RABBITMQ_PASSWORD"

# Endereço do RabbitMQ (valor de SPRING_RABBITMQ_HOST)
RABBITMQ_HOST=$(az container show \
  --name $RABBITMQ_CONTAINER_NAME \
  --resource-group $RESOURCE_GROUP_NAME \
  --query ipAddress.fqdn \
  --output tsv)

echo
echo "=================================================================="
echo " Valores do RabbitMQ para as Variables das pipelines (ou Variable Group):"
echo "   SPRING_RABBITMQ_HOST     = $RABBITMQ_HOST"
echo "   SPRING_RABBITMQ_PORT     = 5672"
echo "   SPRING_RABBITMQ_USERNAME = $RABBITMQ_USER"
echo "   SPRING_RABBITMQ_PASSWORD = (a senha que você digitou; marque como secreta)"
echo " Painel de administração: http://$RABBITMQ_HOST:15672"
echo "=================================================================="
