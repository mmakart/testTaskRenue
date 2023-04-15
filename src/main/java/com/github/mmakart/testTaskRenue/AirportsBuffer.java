package com.github.mmakart.testTaskRenue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class AirportsBuffer {
	private ByteArrayOutputStream baos;
	private DeflaterOutputStream deflaterOut;
	private DataOutputStream dataOut;

	private byte[] compressed;

	private ByteArrayInputStream bais;
	private InflaterInputStream inflaterIn;
	private DataInputStream dataIn;

	private int airportsRemain;
	private int airportsCount = 0;

	public AirportsBuffer() throws IOException {
		baos = new ByteArrayOutputStream();
		deflaterOut = new DeflaterOutputStream(baos, new Deflater(Deflater.BEST_SPEED, false));
		dataOut = new DataOutputStream(deflaterOut);
		airportsRemain = 0;
	}

	public void addAirport(Airport airport) throws IOException {
		Object[] values = airport.getValues();

		dataOut.writeInt((Integer) values[0]);
		dataOut.writeUTF((String) values[1]);
		dataOut.writeUTF((String) values[2]);
		dataOut.writeUTF((String) values[3]);
		dataOut.writeUTF((String) values[4]);
		dataOut.writeUTF((String) values[5]);
		dataOut.writeDouble((Double) values[6]);
		dataOut.writeDouble((Double) values[7]);
		dataOut.writeInt((Integer) values[8]);
		dataOut.writeUTF((String) values[9]);
		dataOut.writeUTF((String) values[10]);
		dataOut.writeUTF((String) values[11]);
		dataOut.writeUTF((String) values[12]);
		dataOut.writeUTF((String) values[13]);

		airportsRemain++;
	}

	public void store() throws IOException {
		airportsCount  = airportsRemain;
		deflaterOut.finish();
		
		compressed = baos.toByteArray();
		
		bais = new ByteArrayInputStream(compressed);
		inflaterIn = new InflaterInputStream(bais, new Inflater(false));
		dataIn = new DataInputStream(inflaterIn);
	}

	public boolean hasNextAirport() {
		return airportsRemain > 0;
	}

	public Airport nextAirport() throws IOException {
		airportsRemain--;
		
		Integer v0 = dataIn.readInt();
		String v1 = dataIn.readUTF();
		String v2 = dataIn.readUTF();
		String v3 = dataIn.readUTF();
		String v4 = dataIn.readUTF();
		String v5 = dataIn.readUTF();
		Double v6 = dataIn.readDouble();
		Double v7 = dataIn.readDouble();
		Integer v8 = dataIn.readInt();
		String v9 = dataIn.readUTF();
		String v10 = dataIn.readUTF();
		String v11 = dataIn.readUTF();
		String v12 = dataIn.readUTF();
		String v13 = dataIn.readUTF();
		
		return new Airport(v0, v1, v2, v3, v4, v5, v6, 
				v7, v8, v9, v10, v11, v12, v13);
	}

	public void rewind() throws IOException {
		bais = new ByteArrayInputStream(compressed);
		inflaterIn = new InflaterInputStream(bais, new Inflater(false));
		dataIn = new DataInputStream(inflaterIn);
		airportsRemain = airportsCount;
	}

}
