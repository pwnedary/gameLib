/**
 * 
 */
package org.gamelib.util.net;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pwnedary
 */
@SuppressWarnings("unchecked")
public class Serialization {
	public static Map<Class<?>, Serializer<?>> serializers = new HashMap<>();

	public <T> void write(ByteBuffer buffer, T object) {
		Output output = new Output(new ByteBufferOutputStream(buffer));

		Class<T> type = (Class<T>) object.getClass();
		output.writeString(type.getName());

		if (object instanceof Serializer<?>) ((Serializer<T>) object).write(output, object);
		else ((Serializer<T>) serializers.get(type)).write(output, object);

		output.flush();
	}

	public <T> T read(ByteBuffer buffer) {
		Input input = new Input(new ByteBufferInputStream(buffer));

		T value = null;
		try {
			Class<T> type = (Class<T>) Class.forName(input.readString());
			Serializer<T> serializer = (Serializer<T>) serializers.get(type);
			if (serializer == null) try {
				serializers.put(type, serializer = (Serializer<T>) type.newInstance());
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}
			value = serializer.read(input);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return (T) value;
	}

	public void writeLength(ByteBuffer buffer, int length) {
		buffer.putInt(length);
	}

	public int readLength(ByteBuffer buffer) {
		return buffer.getInt();
	}

	public int getLengthLength() {
		return 4;
	}

	static {
		serializers.put(Boolean.class, new BooleanSerializer());
		serializers.put(Byte.class, new ByteSerializer());
		serializers.put(Character.class, new CharSerializer());
		serializers.put(Short.class, new ShortSerializer());
		serializers.put(Integer.class, new IntSerializer());
		serializers.put(Long.class, new LongSerializer());
		serializers.put(Float.class, new FloatSerializer());
		serializers.put(Double.class, new DoubleSerializer());
		serializers.put(String.class, new StringSerializer());
	}

	private static class BooleanSerializer implements Serializer<Boolean> {
		/** {@inheritDoc} */
		@Override
		public void write(Output NewOutput, Boolean object) {
			NewOutput.writeBoolean(object);
		}

		/** {@inheritDoc} */
		@Override
		public Boolean read(Input NewInput) {
			return NewInput.readBoolean();
		}
	}

	private static class ByteSerializer implements Serializer<Byte> {
		public void write(Output NewOutput, Byte object) {
			NewOutput.writeByte(object);
		}

		public Byte read(Input NewInput) {
			return NewInput.readByte();
		}
	}

	private static class CharSerializer implements Serializer<Character> {
		public void write(Output NewOutput, Character object) {
			NewOutput.writeChar(object);
		}

		public Character read(Input NewInput) {
			return NewInput.readChar();
		}
	}

	private static class ShortSerializer implements Serializer<Short> {
		public void write(Output NewOutput, Short object) {
			NewOutput.writeShort(object);
		}

		public Short read(Input NewInput) {
			return NewInput.readShort();
		}
	}

	private static class IntSerializer implements Serializer<Integer> {
		public void write(Output NewOutput, Integer object) {
			NewOutput.writeInt(object);
		}

		public Integer read(Input NewInput) {
			return NewInput.readInt();
		}
	}

	private static class LongSerializer implements Serializer<Long> {
		public void write(Output NewOutput, Long object) {
			NewOutput.writeLong(object);
		}

		public Long read(Input NewInput) {
			return NewInput.readLong();
		}
	}

	private static class FloatSerializer implements Serializer<Float> {
		public void write(Output NewOutput, Float object) {
			NewOutput.writeFloat(object);
		}

		public Float read(Input NewInput) {
			return NewInput.readFloat();
		}
	}

	private static class DoubleSerializer implements Serializer<Double> {
		public void write(Output NewOutput, Double object) {
			NewOutput.writeDouble(object);
		}

		public Double read(Input NewInput) {
			return NewInput.readDouble();
		}
	}

	private static class StringSerializer implements Serializer<String> {
		public void write(Output NewOutput, String object) {
			NewOutput.writeString(object);
		}

		public String read(Input NewInput) {
			return NewInput.readString();
		}
	}
}