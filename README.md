# Student Manager Cli

Esse é uma das atividades cobradas na disciplina **MAC0439 - Laboratório de Bancos de Dados**, onde tivemos que implementar uma aplicação que se comunicasse com um banco de dados através do padrão "[Objeto de acesso a dados](https://pt.wikipedia.org/wiki/Objeto_de_acesso_a_dados)" (DAO).

Para esse projeto, a comunicação foi feita através de linha de comando interativa. Por não ser o foco da atividade, o seu comportamento quanto aos inputs dos usuários não é totalmente estável.

## Observações importantes

1. Antes de compilar os arquivos, edite o arquivo "/src/db/ConnectionFactory.java", corrigindo as informações
de conexão para as suas informações de BD (URL do BD), usuário e senha. Elas estão localizadas nas linhas 13 a
15 do arquivo. A URL que deve ser colocada deve ser do formato "jdbc:postgresql://[DOMÍNIO]:[PORTA (padrão
5432)]/[NOME DO BANCO A ACESSAR]".

2. Para que o sistema funcione, primeiro crie as tabelas em um banco de dados executando o script de criação
do BD "script_criacao_bd_alunos.sql", incluído no envio. Caso não especifique um banco específico para
criação, as tabelas serão criadas e populadas em um banco com o nome do usuário que executou o script. Use o
nome do banco na URL de acesso da observação 1.

3. O arquivo incluído no classpath no comando de execução é o driver JDBC que possibilita a conexão de um
programa Java com o PostgreSQL. Ele é para Java versão 8 ou superior. Caso você esteja usando uma versão mais
antiga de Java, baixe a versão apropriada do driver de <https://jdbc.postgresql.org/download.html>.



## Como compilar

Para compilar o sistema, abra um terminal no diretório raiz do exercício e execute:

$ javac src/**/*.java


## Como executar

Para executar o programa, ainda no diretório raiz, execute:

$ java -classpath ./src:./postgresql-42.2.14.jar cli.RegistrationSystem

Isso irá gerar um processo iterativo que estará esperando por um comando do usuário.


## Como funciona

O programa exige que se entre inicialmente algum dos comandos implementados para iniciar uma interação
específica. Os comandos existentes são os seguintes:

### "inserir [aluno/professor/curso]"

Esse é o comando responsável por inserir tuplas no banco de dados. A segunda palavra deve ser "aluno",
"professor" ou "curso", onde ela representa sobre qual banco de dados essa operação será realizada. Qualquer
outra palavra irá gerar um erro e retornar para o prompt de seleção de comandos.

Ao se entrar no modo de inserção, o terminal irá permitir que o usuário introduza o conteúdo das colunas de
uma das tabelas especificadas do banco de dados. Será pedido cada campo de cada vez, onde o terminal espera
que o usuário digite o valor que deseja inserir. Não serão mostrados diretamente qual campo será preenchido,
mas sim uma descrição desse campo. O valor será internamente convertido para o tipo apropriado e, caso não
seja possível fazer a conversão, irá gerar um erro ao fim do processo de inserção.

Obs: caso usuário não queira preencher algum campo opcional, deixar vazio irá torná-lo nulo no banco de dados.
Isso irá gerar erro caso o campo seja uma chave primária ou seja não nulo.

### "alterar [aluno/professor/curso]"

Esse comando altera tuplas do banco de dados. A segunda palavra deve ser "aluno", "professor" ou "curso", onde
ela representa sobre qual banco de dados essa operação será realizada. Qualquer outra palavra irá gerar um
erro e retornar para o prompt de seleção de comandos.

Ao se entrar no modo de alteração, o terminal irá pedir pelos dados da(s) coluna(s) que são
chave(s)-primária(s), ou seja, que identifiquem univocamente uma tupla do banco de dados. Caso encontre um
resultado, irá pedir para que o usuário entre com os novos valores. Além disso, mostrará o valor que está no
banco de dados quando a tupla foi encontrada. Caso o usuário queira manter esse valor, basta deixar o campo em
branco. Caso deseje removê-lo (colocar null no banco), basta escrever "null" no campo de alteração. Depois de
serem introduzidos todos os valores apresentados, espera-se por um pedido de confirmação ou negação. Caso seja
confirmada, a operação altera o banco de dados.

Obs: Os atributos que são possíveis de serem alterados são aqueles que não são considerados como
chaves-primárias.

### "remover [aluno/professor/curso]"

Esse comando remove tuplas do banco de dados. A segunda palavra deve ser "aluno", "professor" ou "curso", onde
ela representa sobre qual banco de dados essa operação será realizada. Qualquer outra palavra irá gerar um
erro e retornar para o prompt de seleção de comandos.

Ao se entrar no modo de remoção, procura-se por uma tupla, da mesma forma que no processo de alteração
(descrito acima). Caso seja encontrada uma tupla do banco, pede-se uma confirmação de deleção da tupla
encontrada (que é mostrada no terminal). Caso seja aceita, remove-se a tupla do banco.

### "procurar curso"

Esse comando faz uma busca de cursos a partir de até duas keywords: o nome da matéria (ou parte dele) e/ou o
nome do professor que a leciona (ou parte dele), e devolve informações sobre os cursos encontrados.

Ao se iniciar o processo, são pedidos ambas as informações necessárias para filtrar os resultados. Caso um
campo esteja vazio, ele não afeta a filtragem. Caso ambos estejam vazios, todos os cursos são retornados. São
retornadas as informações sobre os cursos e, no fim, quantos cursos com o perfil descrito foram encontrados

Obs: os termos de busca são case-insensitive

### "matricular aluno"

Esse comando faz a matrícula de um aluno em um conjunto de cursos definido a partir do resultado da busca de
cursos acima.

Ao entrar nesse processo, tenta-se encontrar o aluno que será matriculado. Se for encontrado, são pedidos os
termos de busca de forma similar ao processo de procura de cursos acima. Caso seja encontrado um ou mais
cursos, mostra-se quais cursos o aluno será matriculado e pede uma confirmação do usuário. Caso ele aprove, o
aluno é matriculado naquelas disciplinas descritas.

Obs: Caso o aluno já esteja matriculado em uma das disciplinas mencionadas, ocorrerá um erro no Postgres, mas
ainda serão adicionados as outras matrículas que não existirem.
