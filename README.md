# 🏆 FourQuiz - Quiz App Multidisciplinar

O **FourQuiz** é um aplicativo Android robusto focado em gamificação de conhecimento, desenvolvido como projeto final para a disciplina de Programação para Dispositivos Móveis. O projeto implementa uma arquitetura moderna e escalável, utilizando o ecossistema oficial do Google para persistência e autenticação.

---

## 📸 Screenshots

Para visualizar o funcionamento do app, veja as capturas de tela abaixo:

<div align="center">
  <img src="./screenshots/login.png" width="200" title="Tela de Login">
  <img src="./screenshots/home.png" width="200" title="Dashboard">
  <img src="./screenshots/quiz.png" width="200" title="Execução do Quiz">
  <img src="./screenshots/ranking.png" width="200" title="Ranking Global">
</div>

---

## 🚀 Tecnologias e Decisões Técnicas

Para garantir um código limpo e de fácil manutenção, seguimos as melhores práticas de desenvolvimento Android:

* **Linguagem:** Kotlin 1.9+
* **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Arquitetura Declarativa)
* **Arquitetura:** MVVM (Model-View-ViewModel) + Clean Architecture
* **Injeção de Dependência:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
* **Banco de Dados:** Cloud Firestore (NoSQL)
* **Autenticação:** Firebase Auth (E-mail/Senha e Google Sign-In)
* **Async:** Kotlin Coroutines & Flow (Processamento assíncrono e reativo)

---

## 🛠️ Requisitos Acadêmicos Atendidos

| Requisito | Descrição | Status |
| :--- | :--- | :---: |
| **REQ1** | Autenticação Firebase + Perfil Sincronizado | ✅ |
| **REQ2** | Firestore + Cache Local (Offline-first) | ✅ |
| **REQ3** | Quiz Dinâmico com salvamento local/nuvem | ✅ |
| **REQ4** | Histórico de Desempenho e Ranking Global | ✅ |
| **REQ5** | Interface Material Design 3 e Navigation | ✅ |

---

## 🏗️ Arquitetura do Sistema

O projeto foi estruturado em camadas para permitir o desacoplamento e o desenvolvimento paralelo:

1.  **UI Layer:** Composta por telas em Compose e ViewModels que gerenciam o estado via `StateFlow`.
2.  **Domain Layer (Interfaces):** Contratos de repositório que definem as regras de negócio.
3.  **Data Layer:** Implementação concreta dos repositórios, lidando com chamadas ao Firebase e lógica de cache nativo.



---

## 📦 Como Instalar e Testar

### Opção 1: APK (Recomendado para avaliação)
Para testar o aplicativo diretamente no seu smartphone:
1. Acesse a seção **[Releases](https://github.com/isaaflor/FourQuiz/releases)** deste repositório.
2. Baixe o arquivo `FourQuiz.apk`.
3. Instale o APK no seu dispositivo Android (autorize a instalação de fontes desconhecidas se necessário).

### Opção 2: Compilar via Código-fonte
1. Clone o repositório: `git clone https://github.com/isaaflor/FourQuiz.git`
2. Abra o projeto no **Android Studio Ladybug (ou superior)**.
3. Importe o seu arquivo `google-services.json` na pasta `/app`.
4. Execute o Build e rode em um emulador ou dispositivo físico.

---

## 👥 Equipe de Desenvolvimento

* **Arthur Martins Aguiar**
* **Eduardo Lordão Oliveira**
* **Gabriel Fernandes da Silva**
* **Isabelle Alves Florêncio (isaaflor)**

---
*Este projeto foi desenvolvido com propósitos acadêmicos e demonstra o uso de boas práticas de engenharia de software mobile.*
