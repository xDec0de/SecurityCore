package es.xdec0de.securitycore.api;

/**
 * A class containing all the methods intended for API usage from SecurityCore.
 * 
 * @since v2.0.0
 * 
 * @author xDec0de_
 * 
 * @see #getInstance()
 */
public class SecurityCoreAPI {

	private static SecurityCoreAPI instance;

	private final MCVersion version;

	private SecurityCoreAPI() {
		this.version = MCVersion.getServerVersion();
	}

	/**
	 * Gets the instance of the current {@link SecurityCoreAPI} object being used, creating an instance if the instance is null.
	 * 
	 * @return an instance of {@link SecurityCoreAPI}.
	 */
	public static SecurityCoreAPI getInstance() {
		return instance != null ? instance : (instance = new SecurityCoreAPI());
	}

	/**
	 * Gets the server version.
	 * 
	 * @return The server version as a {@link MCVersion}
	 */
	public MCVersion getServerVersion() {
		return version;
	}
}
