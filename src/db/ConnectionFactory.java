/* Adaptado de Exemplo2-DAO disponibilizado na página da matéria no E-disciplinas ( que foi adaptado
de http://www.caelum.com.br/apostila-java-web/bancos-de-dados-e-jdbc/) */

package db;

import java.sql.*;
import java.util.Properties;

public class ConnectionFactory {

	// MODIFIQUE O VALOR DESTAS CONSTANTES COM OS DADOS DO SEU BD
	protected static final String DRIVER_NAME = "org.postgresql.Driver";
	public static final String URL_DB = "jdbc:postgresql://localhost:5432/meu_bd";
	public static final String USER_DB = "meu_usuario";
	public static final String PASSWORD_DB = "minha_senha";

	private static ConnectionFactory connectionFactory = null;

	private ConnectionFactory() {
		try {
            Class.forName(DRIVER_NAME);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Lembre de fechar a conexão quando acabar de usá-la
	public Connection getConnection() {

		Properties props = new Properties();
		props.setProperty("ssl","false");
		props.setProperty("user", USER_DB);
		props.setProperty("password", PASSWORD_DB);

		try {
			return DriverManager.getConnection(URL_DB, props);
		} catch (SQLException e) {
			// A SQLException é "encapsulada" em uma RuntimeException
			// para desacoplar o código da API de JDBC
			throw new RuntimeException(e);
		}
	}

	public static ConnectionFactory getInstance() {
		// A fábrica é um singleton
		if (connectionFactory == null) {
			connectionFactory = new ConnectionFactory();
		}
		return connectionFactory;
	}

}
