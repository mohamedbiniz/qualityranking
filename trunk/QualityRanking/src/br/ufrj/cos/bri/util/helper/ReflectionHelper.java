package br.ufrj.cos.bri.util.helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Métodos utilitários para facilitar a utilização de Reflection e
 * Annotations.
 * 
 * @author Fabricio Raphael
 * @author Juan Marcus
 * @author Stanley Alves
 * 
 */
public abstract class ReflectionHelper {

	public static Class<?> getPrimitiveClass(Class<?> klass) {
		if (klass.equals(boolean.class)) {
			return Boolean.class;
		} else if (klass.equals(byte.class)) {
			return Byte.class;
		} else if (klass.equals(short.class)) {
			return Short.class;
		} else if (klass.equals(char.class)) {
			return Character.class;
		} else if (klass.equals(int.class)) {
			return Integer.class;
		} else if (klass.equals(long.class)) {
			return Long.class;
		} else if (klass.equals(float.class)) {
			return Float.class;
		} else if (klass.equals(double.class)) {
			return Double.class;
		}
		return null;
	}

	public static String getTableName(Class<?> klass) {
		Table table = klass.getAnnotation(Table.class);
		if (table != null) {
			String result = table.name();
			if (!result.trim().equals("")) {
				return result;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Lista todas as chaves candidatas de uma classe.
	 * 
	 * @param klass -
	 *            A classe a ser procurada.
	 * @return Uma lista de atributos chave.
	 */
	public static List<Field> listCandidateKeys(Class<?> klass) {
		List<Field> fields = new ArrayList<Field>();

		for (Field field : ReflectionHelper.listFields(klass)) {
			if (field.isAnnotationPresent(Column.class)) {
				if (field.getAnnotation(Column.class).unique()) {
					fields.add(field);
				}
			} else if (field.isAnnotationPresent(JoinColumn.class)) {
				if (field.getAnnotation(JoinColumn.class).unique()) {
					fields.add(field);
				}
			} else if (field.isAnnotationPresent(Id.class)) {
				if (!field.isAnnotationPresent(GeneratedValue.class)) {
					fields.add(field);
				}
			}
		}

		return fields;
	}

	/**
	 * Lista todas as chaves candidatas de uma classe e todas as suas
	 * superclasses.
	 * 
	 * @param klass -
	 *            A classe a ser procurada.
	 * @return Uma lista de catributos chave.
	 */
	public static List<Field> listAllCandidateKeys(Class<?> klass) {
		List<Field> fields = new ArrayList<Field>();

		while (!klass.equals(Object.class)) {
			List<Field> aux = listCandidateKeys(klass);
			aux.addAll(fields);
			fields = aux;
			klass = klass.getSuperclass();
		}

		return fields;
	}

	/**
	 * Verifica se dois objetos possuem valores iguais nos seus atributos
	 * identificadores.
	 * 
	 * @param o1 -
	 *            O primeiro objeto.
	 * @param o2 -
	 *            O segundo objeto.
	 * @return <code>true</code> caso os atributos identificadores sejam
	 *         iguais e <code>false</code> caso contrário.
	 */
	public static boolean equalsKey(Object o1, Object o2) {
		Field idField1 = getIdField(o1.getClass());
		if (idField1 == null) {
			return false;
		}
		Field idField2 = getIdField(o2.getClass());
		if (idField2 == null) {
			return false;
		}
		Object idField1Value = getFieldValue(o1, idField1);
		Object idField2Value = getFieldValue(o2, idField2);
		if (idField1Value == null && idField2Value == null) {
			return true;
		}
		if (idField1Value != null && idField1Value.equals(idField2Value)) {
			return true;
		}
		return false;
	}

	public static Class<?> getTargetEntity(Field field) {

		ManyToMany a1 = field.getAnnotation(ManyToMany.class);
		if (a1 != null) {
			return a1.targetEntity();
		}

		ManyToOne a2 = field.getAnnotation(ManyToOne.class);
		if (a2 != null) {
			return a2.targetEntity();
		}

		OneToMany a3 = field.getAnnotation(OneToMany.class);
		if (a3 != null) {
			return a3.targetEntity();
		}

		return null;
	}

	/**
	 * Lista todos os métodos de uma classe.
	 * 
	 * @param klass -
	 *            A classe a ser procurada.
	 * @return Uma lista de métodos.
	 */
	public static List<Method> listMethods(Class<?> klass) {
		return new ArrayList<Method>(Arrays.asList(klass.getDeclaredMethods()));
	}

	/**
	 * Lista todos os métodos de uma classe e todas as suas superclasses.
	 * 
	 * @param klass -
	 *            A classe a ser procurada.
	 * @return Uma lista de métodos.
	 */
	public static List<Method> listAllMethods(Class<?> klass) {
		List<Method> methods = new ArrayList<Method>();

		methods.addAll(Arrays.asList(klass.getMethods()));
		methods.removeAll(listMethods(Object.class));

		return methods;
	}

	/**
	 * Lista todos os atributos de uma classe.
	 * 
	 * @param klass -
	 *            A classe a ser procurada.
	 * @return Uma lista de atributos.
	 */
	public static List<Field> listFields(Class<?> klass) {
		return new ArrayList<Field>(Arrays.asList(klass.getDeclaredFields()));
	}

	/**
	 * Lista todos os atributos de uma classe e todas as suas superclasses.
	 * 
	 * @param klass -
	 *            A classe a ser procurada.
	 * @return Uma lista de atributos.
	 */
	public static List<Field> listAllFields(Class<?> klass) {
		List<Field> fields = new ArrayList<Field>();

		while (!klass.equals(Object.class)) {
			List<Field> aux = listFields(klass);
			aux.addAll(fields);
			fields = aux;
			klass = klass.getSuperclass();
		}

		return fields;
	}

	/**
	 * Lista todos os atributos de uma classe que sejam de um tipo.
	 * 
	 * @param klass -
	 *            A classe a ser procurada.
	 * @param type -
	 *            O tipo dos campos a serem procurados.
	 * @return Uma lista de atributos.
	 */
	public static List<Field> listAllFieldsByType(Class<?> klass, Class<?> type) {
		List<Field> fields = listAllFields(klass);
		List<Field> result = new ArrayList<Field>();
		for (Field field : fields) {
			if (field.getType().equals(type)) {
				result.add(field);
			}
		}
		return result;
	}

	/**
	 * Lista todas as chaves candidatas de uma classe.
	 * 
	 * @param klass -
	 *            A classe a ser procurada.
	 * @return Uma lista de atributos chave.
	 */
	public static List<Field> listUniqueColumns(Class<?> klass) {
		List<Field> fields = new ArrayList<Field>();

		for (Field field : listFields(klass)) {
			if (field.isAnnotationPresent(Column.class)) {
				if (field.getAnnotation(Column.class).unique()) {
					fields.add(field);
				}
			} else if (field.isAnnotationPresent(JoinColumn.class)) {
				if (field.getAnnotation(JoinColumn.class).unique()) {
					fields.add(field);
				}
			} else if (field.isAnnotationPresent(Id.class)) {
				if (!field.isAnnotationPresent(GeneratedValue.class)) {
					fields.add(field);
				}
			}
		}

		return fields;
	}

	/**
	 * Lista todas as chaves candidatas de uma classe e todas as suas
	 * superclasses.
	 * 
	 * @param klass -
	 *            A classe a ser procurada.
	 * @return Uma lista de catributos chave.
	 */
	public static List<Field> listAllUniqueColumns(Class<?> klass) {
		List<Field> fields = new ArrayList<Field>();

		while (!klass.equals(Object.class)) {
			List<Field> aux = listUniqueColumns(klass);
			aux.addAll(fields);
			fields = aux;
			klass = klass.getSuperclass();
		}

		return fields;
	}

	/**
	 * Lista todas as colunas de uma classe. As colunas são aquelas anotadas
	 * com {@link Id}, {@link Column}, {@link JoinColumn}, {@link ManyToOne}
	 * ou {@link ManyToMany}.
	 * 
	 * @param klass -
	 *            A classe a ser procurada.
	 * @return Uma lista de atributos que são colunas no banco.
	 */
	public static List<Field> listColumns(Class<?> klass) {
		List<Field> fields = new ArrayList<Field>();

		for (Field field : listFields(klass)) {
			if (field.isAnnotationPresent(Column.class)
					|| field.isAnnotationPresent(JoinColumn.class)
					|| field.isAnnotationPresent(Id.class)
					|| field.isAnnotationPresent(ManyToOne.class)
					|| field.isAnnotationPresent(ManyToMany.class)) {
				fields.add(field);
			}
		}
		return fields;
	}

	/**
	 * Lista todas as colunas de uma classe e todas as suas superclasses. As
	 * colunas são aquelas anotadas com {@link Id}, {@link Column} ou
	 * {@link JoinColumn}.
	 * 
	 * @param klass -
	 *            A classe a ser procurada.
	 * @return Uma lista de atributos que são colunas no banco.
	 */
	public static List<Field> listAllColumns(Class<?> klass) {
		List<Field> fields = new ArrayList<Field>();

		while (!klass.equals(Object.class)) {
			List<Field> aux = listColumns(klass);
			aux.addAll(fields);
			fields = aux;
			klass = klass.getSuperclass();
		}
		return fields;
	}

	public static List<Field> listRequiredColumns(Class<?> klass) {
		List<Field> fields = listAllColumns(klass);
		List<Field> result = new ArrayList<Field>();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if ((column != null) && (column.nullable() == false)) {
				result.add(field);
				continue;
			}
			JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
			if ((joinColumn != null) && (joinColumn.nullable() == false)) {
				result.add(field);
				continue;
			}
		}
		return result;
	}

	/**
	 * Retorna o atributo identificador de uma classe. Esse atributo é aquele
	 * anotado com a anotação {@link Id}.
	 * 
	 * @param klass -
	 *            A classe a ser procurada.
	 * @return - O atributo identificador ou null caso ele não exista.
	 */
	public static Field getIdField(Class<?> klass) {
		for (Field field : listAllFields(klass)) {
			if (field.isAnnotationPresent(Id.class)) {
				return field;
			}
		}
		return null;
	}

	/**
	 * Lista todos os contrutores de uma classe.
	 * 
	 * @param klass -
	 *            A classe a ser verificada.
	 * @return Uma lista de contrutores.
	 */
	public static List<Constructor<?>> listConstructors(Class<?> klass) {
		return new ArrayList<Constructor<?>>(Arrays.asList(klass
				.getDeclaredConstructors()));
	}

	/**
	 * Procura um método numa classe ou superclasses através do seu nome.
	 * 
	 * @param klass -
	 *            A classe a ser verificada.
	 * @param name -
	 *            O nome do método a ser procurado.
	 * @return O método com o nome especificado ou null caso ele não seja
	 *         encontrado.
	 */
	public static Method findMethod(Class<?> klass, String name) {
		for (Method method : listAllMethods(klass)) {
			if (method.getName().equals(name)) {
				return method;
			}
		}
		return null;
	}

	/**
	 * Procura um atributo numa classe ou superclasses através do seu nome.
	 * 
	 * @param klass -
	 *            A classe a ser verificada.
	 * @param name -
	 *            O nome do atributo a ser procurado.
	 * @return O atributo com o nome especificado ou null caso ele não seja
	 *         encontrado.
	 */
	public static Field findField(Class<?> klass, String name) {
		for (Field field : listAllFields(klass)) {
			if (field.getName().equals(name)) {
				return field;
			}
		}
		return null;
	}

	/**
	 * Chama um método de acesso a um atributo de uma classe.
	 * 
	 * @param acessorType -
	 *            O tipo de acesso a ser feito.
	 * @param obj -
	 *            O objeto onde o método será executado.
	 * @param field -
	 *            O atributo que se deseja acessar.
	 * @param newValue -
	 *            O valor que deve ser colocado no atributo, caso o acessorType
	 *            seja {@link AcessorType#SETTER}.
	 * @return O valor de retorno da execução do método. O retorno pode ser
	 *         {@link Void} caso o método não retorne nada.
	 */
	private static Object callAccessorMethod(AcessorType acessorType,
			Object obj, Field field, Object newValue) {

		String methodName = Convention.getMethodName(acessorType, field);

		Method method = findMethod(obj.getClass(), methodName);

		Object result = null;

		if (method != null) {
			try {
				switch (acessorType) {
				case GETTER:
					result = method.invoke(obj);
					break;
				case SETTER:
					result = method.invoke(obj, newValue);
					break;
				default:
					break;
				}
				return result;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * Retorna o valor de um atributo de um objeto. O atributo precisa ter um
	 * método Getter de acordo com o padrão Bean.
	 * 
	 * @param obj -
	 *            O objeto de onde será lido o atributo.
	 * @param field -
	 *            O campo que será lido.
	 * @return O valor do campo no objeto.
	 */
	public static Object getFieldValue(Object obj, Field field) {
		return callAccessorMethod(AcessorType.GETTER, obj, field, null);
	}

	/**
	 * Define o valor de um atributo de um objeto. O atributo precisa ter um
	 * método Setter de acordo com o padrão Bean.
	 * 
	 * @param obj -
	 *            O objeto onde será atributo o valor.
	 * @param field -
	 *            O campo onde será colocado o valor.
	 * @param newValue -
	 *            o valor que será colocado.
	 */
	public static void setFieldValue(Object obj, Field field, Object newValue) {
		callAccessorMethod(AcessorType.SETTER, obj, field, newValue);
	}

	/**
	 * Verifica se uma classe herda de outra.
	 * 
	 * @param klass -
	 *            A classe a ser verificada.
	 * @param c -
	 *            A classe base
	 * @return <code>true</code> se a classe a ser verificada herdar da classe
	 *         base ou <code>false</code> caso contrário.
	 */
	public static boolean inheritsFrom(Class<?> klass, Class<?> c) {
		return c.isAssignableFrom(klass);
	}

	/**
	 * Retorna o nome do campo identificador de uma classe.
	 * 
	 * @param klass -
	 *            A classe a ser verificada.
	 * @return O nome do campo identificador.
	 */
	public static String getIdFieldName(Class<?> klass) {
		Field idField = ReflectionHelper.getIdField(klass);
		if (idField != null) {
			return idField.getName();
		} else {
			return null;
		}
	}

	public static Object invokeGetMethod(Object obj, String fieldName) {
		String methodName = Convention.getMethodName(AcessorType.GETTER,
				fieldName);

		Method method = findMethod(obj.getClass(), methodName);

		try {
			return method.invoke(obj);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
