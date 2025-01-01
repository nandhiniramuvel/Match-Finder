package com.app.model;

import java.util.Objects;

public class Result {
	
	private final int lineOffset;
	private final int charOffset;

	public Result(int lineOffset, int charOffset) {
		this.lineOffset = lineOffset;
		this.charOffset = charOffset;
	}

	@Override
	public String toString() {
		return "[lineOffset=" + lineOffset + ", charOffset=" + charOffset + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(charOffset, lineOffset);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Result other = (Result) obj;
		return charOffset == other.charOffset && lineOffset == other.lineOffset;
	}

}