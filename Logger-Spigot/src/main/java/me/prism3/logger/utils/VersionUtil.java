package me.prism3.logger.utils;

import org.bukkit.Bukkit;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a parsed CraftBukkit version (e.g. v1_19_R3).
 * Provides easy numeric comparisons (isAtLeast, isOlderThan, etc).
 */
public final class VersionUtil implements Comparable<VersionUtil> {

    private static final Pattern VERSION_PATTERN =
            Pattern.compile("^v(\\d+)_(\\d+)_R(\\d+)$");

    /** The detected server version at startup. */
    public static final VersionUtil CURRENT = parseFromPackage();

    private final int major;
    private final int minor;
    private final int revision;

    private VersionUtil(int major, int minor, int revision) {
        this.major = major;
        this.minor = minor;
        this.revision = revision;
    }

    /** Parse from the CraftBukkit/CraftServer package suffix. */
    private static VersionUtil parseFromPackage() {

        String pkg = Bukkit.getServer().getClass().getPackage().getName();
        String suffix = pkg.substring(pkg.lastIndexOf('.') + 1); // e.g. "v1_19_R3"
        Matcher m = VERSION_PATTERN.matcher(suffix);

        if (m.matches()) {
            int ma = Integer.parseInt(m.group(1));
            int mi = Integer.parseInt(m.group(2));
            int rv = Integer.parseInt(m.group(3));
            return new VersionUtil(ma, mi, rv);
        }
        // Fallback: assume latest known
        return new VersionUtil( Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE );
    }

    /** True if this server version ≥ the given target. */
    public boolean isAtLeast(int targMajor, int targMinor, int targRevision) {
        return this.compareTo(new VersionUtil(targMajor, targMinor, targRevision)) >= 0;
    }

    /** True if this server version ≥ the given other. */
    public boolean isAtLeast(VersionUtil other) {
        return this.compareTo(other) >= 0;
    }

    /** True if this server version < the given target. */
    public boolean isOlderThan(int targMajor, int targMinor, int targRevision) {
        return this.compareTo(new VersionUtil(targMajor, targMinor, targRevision)) < 0;
    }

    /** True if this is a “legacy” version (pre‑1.13). */
    public boolean isLegacy() {
        return isOlderThan(1,13,0);
    }

    /** True if this is “modern” (1.13+). */
    public boolean isModern() {
        return !isLegacy();
    }

    @Override
    public int compareTo(VersionUtil o) {
        if (this.major != o.major)   return Integer.compare(this.major, o.major);
        if (this.minor != o.minor)   return Integer.compare(this.minor, o.minor);
        return Integer.compare(this.revision, o.revision);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VersionUtil)) return false;
        VersionUtil v = (VersionUtil) o;
        return major == v.major && minor == v.minor && revision == v.revision;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, revision);
    }

    @Override
    public String toString() {
        return "v" + major + "_" + minor + "_R" + revision;
    }
}
