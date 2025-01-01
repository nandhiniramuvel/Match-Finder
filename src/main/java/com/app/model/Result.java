package com.app.model;

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
}