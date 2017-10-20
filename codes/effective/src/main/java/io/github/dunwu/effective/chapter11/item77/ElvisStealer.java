// "Stealer" class - Page 310
package io.github.dunwu.effective.chapter11.item77;

import java.io.Serializable;

public class ElvisStealer implements Serializable {
	static Elvis impersonator;
	private Elvis payload;

	private Object readResolve() {
		// Save a reference to the "unresolved" Elvis instance
		impersonator = payload;

		// Return an object of correct type for favorites field
		return new String[] { "A Fool Such as I" };
	}

	private static final long serialVersionUID = 0;
}
