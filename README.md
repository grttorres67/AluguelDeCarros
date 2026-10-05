# Aluguel de Carros Ramyres

Aplicativo Android nativo desenvolvido para pequenos locadores de veículos gerenciarem sua frota, clientes através da agenda do dispositivo e locações ativas.

## Funcionalidades

- **Dashboard (RF01):** Tela inicial listando exclusivamente locações ativas, exibindo dados do veículo, cliente, datas (`dd/MM/yyyy`), dias restantes e destaque visual para locações atrasadas (`diasRestantes < 0`), além de FAB para nova locação.
- **Frota de Veículos (RF02):** Listagem de veículos com status (`DISPONIVEL`, `ALUGADO`, `MANUTENCAO`) e formulário de cadastro com validação robusta de placa (padrão antigo `AAA-1234` e Mercosul `AAA1A23`) e diária positiva.
- **Contatos (RF03):** Solicitação de permissão em tempo real (`READ_CONTACTS`), tratamento de negação com opção de tentativa, consulta via `ContentResolver` e `ContactsContract`, e busca em tempo real por nome.
- **Nova Locação (RF04):** Seleção restrita a veículos `DISPONIVEL`, cliente escolhido da agenda, seleção de datas com DatePicker do Material 3, cálculo automático de diárias e valor total, transação atômica no Room (locação ativa + veículo alugado) e atualização em tempo real no Dashboard.

## Tecnologias Utilizadas

- **Linguagem:** Kotlin
- **Interface:** 100% Jetpack Compose + Material Design 3
- **Arquitetura:** MVVM com separação clara entre UI, Domain e Data
- **Banco de Dados:** Room (com Foreign Keys, `@Relation` e transações)
- **Rede / API:** Retrofit 2 com Gson (estruturado para sincronização de veículos e locações)
- **Concurrencia:** Kotlin Coroutines & Flow (`StateFlow`, `collectAsStateWithLifecycle`)
- **Navegação:** Navigation Compose com `SavedStateHandle` (Single Activity)
- **Android System:** `ContentResolver` & Runtime Permissions (`READ_CONTACTS`)

## Arquitetura

```text
UI (Jetpack Compose / Composables)
↓
ViewModel (StateFlow & Coroutines)
↓
Repository (VehicleRepository, ClientRepository, RentalRepository)
↓
Data Source (Room Database / Retrofit API / ContentResolver)
```

## Como Executar

1. Abra o projeto no **Android Studio**.
2. Aguarde o **Gradle Sync** concluir.
3. Conecte um dispositivo físico ou inicie um emulador Android.
4. Execute o app (`assembleDebug`).
5. Conceda a permissão de leitura de contatos ao selecionar um cliente.

## Permissões

- `READ_CONTACTS`: Necessária para importar os clientes diretamente da agenda do dispositivo.

## API REST (Retrofit 2)

O aplicativo utiliza Retrofit 2 configurado com `GsonConverter` para integração e sincronização remota de dados de veículos e locações.
