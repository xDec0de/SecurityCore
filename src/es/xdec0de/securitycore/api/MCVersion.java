package es.xdec0de.securitycore.api;

import org.bukkit.Bukkit;

public enum MCVersion {

	V1_18(18, "1.18"),
	V1_17(17, "1.17"),
	V1_16(16, "1.16"),
	V1_15(15, "1.15"),
	V1_14(14, "1.14"),
	V1_13(13, "1.13"),
	V1_12(12, "1.12"),
	V1_11(11, "1.11"),
	V1_10(10, "1.10"),
	V1_9(9, "1.9"),
	V1_8(8, "1.8"),
	V1_7(7, "1.7"),
	V1_6(6, "1.6"),
	V1_5(5, "1.5"),
	V1_4(4, "1.4"),
	V1_3(3, "1.3"),
	V1_2(2, "1.2"),
	V1_1(1, "1.1"),
	V1_0(0, "1.0"),
	UNKNOWN(999, "Unknown");

	private final int verID;
	final String mcVer;

	MCVersion(int verID, String mcVer) {
		this.verID = verID;
		this.mcVer = mcVer;
	}

	static MCVersion getServerVersion() {
		String bukkitVer = Bukkit.getVersion();
		for(MCVersion version : MCVersion.values())
			if(bukkitVer.contains(version.mcVer))
				return version;
		return UNKNOWN;
	}

	public boolean isSupported() {
		return this == UNKNOWN ? false : this.verID >= 7;
	}

	public String getName() {
		return mcVer;
	}

	public boolean supports(MCVersion version) {
		return verID >= version.verID;
	}
}
