package com.doomengine.system;

public class Version {

	private final int major;

	private final int minor;

	private final int revision;

	public Version(int major, int minor, int revision) {
		this.major = major;
		this.minor = minor;
		this.revision = revision;
	}

	public Version(String version) {
		String v[] = version.split("\\.");
		if (v.length != 3)
			throw new DoomException("the given version does not match the major,minor,revision format:" + version);

		try {
			this.major = Integer.valueOf(v[0]);
			this.minor = Integer.valueOf(v[1]);
			this.revision = Integer.valueOf(v[2]);
		} catch (Exception e) {
			throw new DoomException("the given version numbers must be intergers: " + version);
		}
	}

	@Override public String toString() {
		return major + "." + minor + "." + revision;
	}

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

	public int getRevision() {
		return revision;
	}

}
