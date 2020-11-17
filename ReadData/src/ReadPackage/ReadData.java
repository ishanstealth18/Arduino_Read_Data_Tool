package ReadPackage;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.ConnectException;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.TooManyListenersException;

import javax.xml.bind.DatatypeConverter;

import java.io.IOException;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import java.util.BitSet;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import gnu.io.UnsupportedCommOperationException;

public class ReadData {
	
	SerialPort serialPort; 
	SerialPort serialPort2;
	CommPortIdentifier portIdentifier;
	CommPortIdentifier portIdentifier2;
	CommPort commPort;
	CommPort commPort2;
	
	static File file = new File("C:/Users/ishan/eclipse-workspace/ReadData/Data.txt");
	
    
	
	public ReadData() {
		// TODO Auto-generated constructor stub
	
		super();
	}
	
	/**
	 * Description: This function will open the serial port for connection and start reading the data 
	 * @param portName
	 * @throws NoSuchPortException
	 * @throws PortInUseException
	 * @throws UnsupportedCommOperationException
	 * @throws IOException
	 * @throws TooManyListenersException
	 */
	public void connectPort(String portName) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException, TooManyListenersException
	{
		//Identify the port
		portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		
		if(portIdentifier.isCurrentlyOwned())
		{
			System.out.println("Error: Port Busy!!!!");
		}
		else
		{
			//System.out.println("Port name: "+portIdentifier.getName() +" " +"Port owner: "+portIdentifier.getCurrentOwner());
			//Open the port
			commPort = portIdentifier.open(this.getClass().getName(), 5000);
			
			if(commPort instanceof SerialPort)
			{
				serialPort = (SerialPort) commPort;
				
				//Set port configurations
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1 , SerialPort.PARITY_NONE);
				
				//Opening input and output streams
				InputStream in = serialPort.getInputStream();
				OutputStream out = serialPort.getOutputStream();
				
                String input;
                System.out.println("Enter m: ");
                Scanner sc = new Scanner(System.in);
                input = sc.nextLine();
                //Condition to write data to serial port
                if(input.equals("m"))
                {
                	out.write(input.getBytes());
                }
                (new Thread(new SerialReader(in))).start();
                 
			}
		}
	}
	
	//Serial reader class 
		public static class SerialReader implements Runnable
		{
			InputStream in;
			public SerialReader(InputStream in)
			{
				this.in = in;
			}

			public void run() {
				// TODO Auto-generated method stub
			
				 byte[] buffer = new byte[1024];
		         int len = -1;
		       FileOutputStream fos = null;
		       try {
		    	   fos = new FileOutputStream(file);
		       } catch (FileNotFoundException e1) {
		    	   // TODO Auto-generated catch block
		    	   e1.printStackTrace();
		       }
		       DataOutputStream dos = new DataOutputStream(fos);
		       String newLine = System.getProperty("line.separator");
		       
		         //Logic to read the streaming and printing in ASCII
		         try
		         {
		        	 while((len = this.in.read(buffer)) > -1)
		        	 {
		        		 //System.out.println();
		        		 
		        		 for(int i = 0; i<len; i++)
		        		 {
		        			 //System.out.print(buffer[i] +" ");
		        			 int x = buffer[i] & 0xff;
		        			 String tempStr = String.valueOf(x);
		        			 System.out.print(tempStr +" ");
		        			 String tempStr1 = tempStr.replaceAll("^\\s+|\\s+$", "").trim();
		        			 dos.writeChars(tempStr1 +" ");
		        		 }
		        		 dos.flush();
		        		 dos.writeBytes(newLine);
		        		 dos.writeBytes(newLine);
		        		 System.out.println();
		        		 Thread.sleep(500);
		        	 } 	
		         }
		         catch(IOException e)
		         {
		        	 e.printStackTrace();
		         } catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try
		{
			(new ReadData()).connectPort("COM18");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}


	}

}
