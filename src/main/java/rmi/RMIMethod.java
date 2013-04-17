package rmi;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import ar.edu.unq.concurbattle.comunication.Utils;

public class RMIMethod implements Serializable {

	private final Object[] args;
	private final String name;
	private final Class<?>[] paramTypes;

	private static final long serialVersionUID = 4631156171847831311L;

	public RMIMethod(final Method method, final Object[] args) {
		this.name = method.getName();
		this.args = args;
		this.paramTypes = Utils.toClasses(args);
	}

	public Serializable invoke(final Object target) {
		final Class<? extends Object> clazz = target.getClass();
		Method method;
		try {
			method = clazz.getMethod(this.name, this.paramTypes);
			return (Serializable) method.invoke(target, this.args);
		} catch (final NoSuchMethodException e) {
			throw new RMIProxyRuntimeException("Method [" + this.name
					+ "] not found", e);
		} catch (final IllegalArgumentException e) {
			throw new RMIProxyRuntimeException("Error executing [" + this.name
					+ "] with arguments [" + Arrays.toString(this.args) + "]",
					e);
		} catch (final Exception e) {
			throw new RMIProxyRuntimeException("Error executing [" + this.name
					+ "] method", e);
		}

	}

}
