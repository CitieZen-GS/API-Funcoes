#
# VERIFIQUE SE ESTA COM A ASSINATURA CORRRETA
#
# az account show
# az account list -o table
# az account set --subscription "nome-da-assinatura-correta"


# Variáveis
# Este script cria a infraestrutura compartilhada (grupo, Application Insights e plano B1)
# e o Web App da API de funções. As outras APIs usam o mesmo grupo e plano: rode este script primeiro.
RESOURCE_GROUP_NAME="gs-ajudantefamiliar"
WEB_APP_NAME="ajudante-familiar-funcoes"   # igual ao WEB_APP_NAME do azure-pipeline.yaml
APP_SERVICE_PLAN="ajudante-familiar-SP"
LOCATION="brazilsouth"
RUNTIME="JAVA:21-java21"
APP_INSIGHTS_NAME="ai-ajudante-familiar"

# Registrar os Serviços utilizados
az provider register --namespace Microsoft.Web
az provider register --namespace Microsoft.Insights
az provider register --namespace Microsoft.OperationalInsights
az provider register --namespace Microsoft.ServiceLinker

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






