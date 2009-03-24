package helper;

import java.lang.reflect.Field;

/**
 * Faz o gerenciamento de padrões.
 * 
 * @author Fabricio
 * 
 */
public abstract class Convention {

	/**
	 * Retorna o nome de um atributo de acordo com o padrão do framework. Esse
	 * nome é padronizado da seguinte forma: <br>
	 * <br>
	 * NomeDaClasse.nomeDoCampo
	 * 
	 * @param klass -
	 *            A classe do atributo.
	 * 
	 * @param f -
	 *            O atributo.
	 * @return O nome do atributo.
	 */
	public static String getFieldName(Class<?> klass, Field f) {
		StringBuffer sb = new StringBuffer();
		sb.append(klass.getSimpleName());
		sb.append(".");
		sb.append(f.getName());
		return sb.toString();
	}

	/**
	 * Retorna o nome de um atributo de data inicial de acordo com o padrão do
	 * framework. Esse nome é padronizado da seguinte forma: <br>
	 * <br>
	 * NomeDaClasse.nomeDoCampo + "Inicial"
	 * 
	 * @param klass -
	 *            A classe do atributo.
	 * 
	 * @param f -
	 *            O atributo.
	 * @return O nome do atributo.
	 */
	public static String getInicialDateFieldName(Class<?> klass, Field f) {
		StringBuffer sb = new StringBuffer();
		sb.append(klass.getSimpleName());
		sb.append(".");
		sb.append(f.getName());
		sb.append("Inicial");
		return sb.toString();
	}

	/**
	 * Retorna o nome de um atributo de data final de acordo com o padrão do
	 * framework. Esse nome é padronizado da seguinte forma: <br>
	 * <br>
	 * NomeDaClasse.nomeDoCampo + "Final"
	 * 
	 * @param klass -
	 *            A classe do atributo.
	 * 
	 * @param f -
	 *            O atributo.
	 * @return O nome do atributo no.
	 */
	public static String getFinalDateFieldName(Class<?> klass, Field f) {
		StringBuffer sb = new StringBuffer();
		sb.append(klass.getSimpleName());
		sb.append(".");
		sb.append(f.getName());
		sb.append("Final");
		return sb.toString();
	}

	/**
	 * Determina a String para ser colocada no início do nome de um método de
	 * acordo com o {@link AcessorType} que será utilizado.
	 * 
	 * @param acessor -
	 *            O tipo de acesso utilizado.
	 * @param klass -
	 *            O atributo que será acessado.
	 * @return A String para ser colocada no início do nome do método.
	 */
	public static String getAcessorType(AcessorType acessor, Field f) {
		Class<?> klass = f.getType();
		if (acessor == null || klass == null) {
			return null;
		}

		if (acessor == AcessorType.GETTER) {
			if (klass.isAssignableFrom(Boolean.class)
					|| klass.isAssignableFrom(boolean.class)) {
				return "is";
			} else {
				return "get";
			}
		} else {
			return "set";
		}
	}

	/**
	 * Constrói o nome de um método de acesso a um atributo.
	 * 
	 * @param acessorType -
	 *            O tipo de acesso que o método faz.
	 * @param field -
	 *            O campo que ele acessa.
	 * @return - O nome do método de acesso.
	 */
	public static String getMethodName(AcessorType acessorType, Field field) {
		StringBuffer methodName = new StringBuffer();

		methodName.append(field.getName());
		methodName.setCharAt(0, Character
				.toUpperCase(field.getName().charAt(0)));
		methodName.insert(0, Convention.getAcessorType(acessorType, field));

		return methodName.toString();
	}

	public static String getMethodName(AcessorType acessorType, String fieldName) {
		StringBuffer methodName = new StringBuffer();

		methodName.append(fieldName);
		methodName.setCharAt(0, Character.toUpperCase(fieldName.charAt(0)));
		methodName.insert(0, "get");

		return methodName.toString();
	}

	/**
	 * Constrói o nome que uma lista de objetos deve possuir de acordo com sua
	 * classe. O padrão é< <br>
	 * <br>
	 * "NomeDaClasseList".
	 * 
	 * @param klass -
	 *            A classe dos objetos
	 * @return O nome da lista.
	 */
	public static String getListName(Class<?> klass) {
		return klass.getSimpleName() + "List";
	}
}
