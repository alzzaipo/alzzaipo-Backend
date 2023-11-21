package com.alzzaipo.common;

import com.github.f4b6a3.tsid.Tsid;

public class TsidUtil {

	public static String toString(Long id) {
		return Tsid.from(id).encode(62);
	}

	public static Long toLong(String id) {
		return Tsid.decode(id, 62).toLong();
	}
}
