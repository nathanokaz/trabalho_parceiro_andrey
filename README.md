# Tabelas Hash | Projeto RA3
Projeto de processamento e indexação de uma base com cerca de 3 milhões de avaliações de livros da Amazon (2,7 GB) usando estruturas de dados em Java.

**Alunos:** 
  - Hugo S. Jamielniak (`@HugoJamielniak`)
  - João Pedro Macena Correa (`@Macenajp`)
  - Nathan Bueno C. Okazaki (`@nathanokaz`)

<br>

- **Vídeo de apresentação:** https://youtu.be/rAvAJT5a17Y
- **Link do repositório:** https://github.com/nathanokaz/trabalho_parceiro_andrey
- **Link da base de dados**: https://www.kaggle.com/datasets/mohamedbakhet/amazon-books-reviews

<br>

---
## 📌| Sobre o trabalho:
Este trabalho implementa uma Tabela Hash própria com capacidade fixa (`1.000.003` posições) e tratamento de colisões por encadeamento separado. O objetivo é mapear cada usuário ao seu livro melhor avaliado (considerando a média global do livro) e comparar o desempenho de três funções hash: **FNV1a**, **Polinomial (base 31)** e **djb2**.

**Funcionalidades:**
- Leitura e filtragem de avaliações válidas, contabilizando registros descartados;
- Comparação experimental entre as 3 funções hash (colisões, sondagens com sucesso/falha, pior caso e tempos);
- Consulta pontual de usuários a partir do arquivo `user_id.txt`;
- Ranking dos Top 10 livros com maior média e N $\ge$ 100 avaliações;
- Exportação dos resultados em CSV (normal e ordenado por algoritmo próprio) com medição de tempo de cada etapa.

**Restrições:**
- Somente os tipos `int`, `float`, `double`, `char` e `String`, além de vetores desses tipos. Classes empacotadoras (`Integer`, `Double`, `Float`, `Character` etc.) e o atributo `.length` não são permitidos.
- Nenhum método pronto da biblioteca padrão, exceto leitura/escrita de arquivos e `System.nanoTime()`.
- Código inteiramente em português: classes, métodos, variáveis e comentários.

<br>

---
## 📄| Saídas geradas e regras de desempate:
Além das métricas e tabelas exibidas no console, o programa gera dois arquivos CSV na raiz do projeto:
- `usuario_melhor_livro.csv`: Melhor livro de cada usuário (`User_id,profileName,Title,media_livro,N_livro,nota_usuario`).
- `usuario_melhor_livro_ordenado.csv`: Mesma lista ordenada na forma decrescente por `media_livro`.

**Critérios de desempate:**
- **Melhor livro e Ranking Top 10:** maior média global (comparada sem divisão via: somaₐ × Nᵦ > somaᵦ × Nₐ), seguida por maior N e ordem alfabética crescente de `Title`.
- **Lista ordenada de usuários:** maior `media_livro` (decrescente), seguida por maior `N_livro` e ordem alfabética crescente de `User_id`.

<br>

---
## 📁| Estrutura do projeto:
```text
├── src/
│   ├── Main.java                
│   ├── FuncoesHash.java         
│   ├── TabelaHash.java           
│   ├── No.java                 
│   ├── TabelaLivros.java       
│   ├── TabelaUsuarios.java      
│   ├── DadosUsuario.java         
│   ├── LeitorCsv.java            
│   ├── GravadorCsv.java          
│   ├── UtilitariosTexto.java     
│   ├── ExperimentoHash.java      
│   ├── ConsultaUsuarios.java     
│   ├── RankingLivros.java       
│   └── OrdenadorUsuarios.java    
├── teste.csv                    
├── user_id.txt                   
└── .gitignore
```

<br>

---
## ⚙️| Como compilar e executar o projeto:

**Pré-requisitos:**
- Java JDK (versão 11 ou superior) instalado e configurado no `PATH`.
- Os arquivos `Books_rating.csv` (base oficial dos dados com 2,86 GB) e `user_id.txt` na **raiz do projeto**, com a variável `caminho` em `Main.java` apontando para `"Books_rating.csv"`.

<br>

**Via terminal (clonar, compilar e executar):**
```bash
git clone https://github.com/nathanokaz/trabalho_parceiro_andrey.git
cd trabalho_parceiro_andrey
javac -encoding UTF-8 -d out src/*.java
java -Xmx4g -cp out Main
```

> **Observações:**
> - O argumento `-Xmx4g` reserva 4 GB de memória heap para a JVM processar os 3 milhões de registros do arquivo `Books_rating.csv` sem erro de memória (`OutOfMemoryError`). Para testes rápidos com `teste.csv`, `java -cp out Main` já basta.
> - Os testes foram realizados no Intellij IDEA e por isso, para colocar o argumento `-Xmx4g`, é preciso ir até o arquivo `Main` -> `More Actions` (três pontos ao lado de `Run`) -> `Add a new configuration` -> `Application` -> Pressionar as teclas "Alt" + "M" -> Clicar em `Add VM option` -> Na caixa de texto `VM options` coloque: `-Xmx4g` -> Clique em `Apply` e em seguida `Run`.

