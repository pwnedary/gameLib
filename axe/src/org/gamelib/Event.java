/**
 * 
 */
package org.gamelib;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import org.gamelib.backend.Input;
import org.gamelib.graphics.GL10;

/** An Event that occurred; may hold additional information.
 * 
 * @author pwnedary */
public interface Event {
	/** Returns whether this {@link Event} has been cancelled and won't get published to underlying handlers.
	 * 
	 * @return <tt>true</tt> if cancelled */
	public boolean cancelled();

	/** Stops the underlying handlers from receiving this event. */
	public void cancel();

	/** Returns the object that caused this {@link Event} <i>(optional operation)</i>.
	 * 
	 * @return the object that caused this Event
	 * @throws UnsupportedOperationException If the cause was unknown/specified. */
	public Object source();

	public static abstract class EventImpl implements Event {
		/** If to stop notifying underlying handlers. */
		public boolean cancelled;
		/** Optionally the object that caused this Event. */
		public Object source;

		@Override
		public boolean cancelled() {
			return cancelled;
		}

		@Override
		public void cancel() {
			cancelled = true;
		}

		@Override
		public Object source() {
			if (source == null) throw new UnsupportedOperationException("No source was declared.");
			return source;
		}
	}

	/** Event triggered each tick. */
	public static class Tick extends EventImpl implements Event {
		public final float delta;

		public Tick(float delta) {
			this.delta = delta;
		}
	}

	/** Event triggered when the screen redraws. */
	public static final class Draw extends EventImpl implements Event {
		public final GL10 gl;
		public final float delta;

		public Draw(GL10 gl, float delta) {
			this.gl = gl;
			this.delta = delta;
		}
	}

	/** Abstract event for input actions. */
	public abstract static class Control<T extends InputEvent> extends EventImpl implements Event {
		public final Input input;

		public Control(Input input) {
			this.input = input;
		}
	}

	public static final class Key extends Control<KeyEvent> {
		public final int id;
		public final int keyCode;

		public Key(Input input, int id, int keyCode) {
			super(input);
			this.id = id;
			this.keyCode = keyCode;
		}
	}

	public static final class Mouse extends Control<MouseEvent> {
		public final int id;
		public final int button;

		public Mouse(Input input, int id, int button) {
			super(input);
			this.id = id;
			this.button = button;
		}
	}

	public static final class MouseWheel extends Control<MouseWheelEvent> {
		public final double scrollAmount;

		public MouseWheel(Input input, double scrollAmount) {
			super(input);
			this.scrollAmount = scrollAmount;
		}
	}

	public static final class Create extends EventImpl implements Event {}

	public static final class Dispose extends EventImpl implements Event {}

	public static final class Resize extends EventImpl implements Event {
		public final int newWidth;
		public final int newHeight;

		public Resize(int newWidth, int newHeight) {
			this.newWidth = newWidth;
			this.newHeight = newHeight;
		}
	}
}
