# Verbax - Gestão de Biblioteca

**Verbax** é uma aplicação desktop desenvolvida em Java com JavaFX que permite a gestão completa de uma biblioteca. O sistema permite a inserção, atualização, exclusão e consulta de livros, autores, editoras, temas e localizações.

Este projeto foi desenvolvido com o objetivo de servir como **avaliação escolar** para a unidade de Programação no curso de Informática - 2GI.

## 🛠️ Tecnologias Utilizadas

- Java 24
- JavaFX
- MySQL
- JDBC
- Scene Builder (FXML)
- IntelliJ IDEA

## 📂 Estrutura do Projeto

```
Verbax/
├── src/
│   ├── application/        # Classe principal do projeto (Main.java)
│   ├── controller/         # Controladores JavaFX (MVC)
│   ├── dao/                # Camada de acesso a dados (Data Access Object)
│   ├── model/              # Classes de modelo (Entidades)
│   ├── util/               # Utilitários (ex: conexões)
│   └── view/               # Arquivos FXML (interfaces gráficas)
├── lib/                    # Bibliotecas externas (se necessário)
├── .idea/                  # Configurações do IntelliJ IDEA
├── .gitignore              # Arquivos ignorados pelo Git
├── LICENSE                 # Licença do projeto
└── DanyloDuma2GI_Java.iml  # Arquivo de projeto do IntelliJ
```

## 📋 Funcionalidades

- 👤 **Gestão de Autores:** inserção, atualização, exclusão, pesquisa.
- 📚 **Gestão de Livros:** com associação a autores, editoras, temas e localização.
- 🏷️ **Gestão de Temas e Editoras.**
- 🗺️ **Gestão de Localizações:** controla onde o livro está fisicamente localizado.
- 🔎 **Pesquisa por ID.**
- ✅ **Validação de campos** para evitar entradas inválidas.
- 📄 **Interface gráfica em JavaFX** utilizando FXML.

## 🧪 Como Executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/DanyloDuma/verbax.git
   cd verbax
   ```

2. Importe o projeto em uma IDE como IntelliJ IDEA.

3. Configure a conexão com o MySQL em `src/util/DBConnection.java`

4. Execute a aplicação pela classe `Main.java`.

5. Certifique-se de ter o banco de dados MySQL criado com as tabelas:
    - `autor`
    - `livro`
    - `editora`
    - `tema`
    - `localizacao`
6. Certifique-se em ter instalado a biblioteca JavaFX e o driver JDBC.

## 🧱 Modelo de Dados

Cada entidade possui um DAO e um Controller dedicado. As operações CRUD são realizadas via JDBC diretamente.

## 🧑‍💻 Desenvolvedor

- **Nome:** Danylo Duma
- **Curso:** 2º ano de Informática - 2GI
- **Objetivo:** Projeto escolar para a unidade de Programação.

## 📄 Licença

Este projeto está licenciado sob os termos da [MIT License](LICENSE).
