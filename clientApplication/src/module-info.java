module clientApplication {
	requires java.desktop;
	requires java.rmi;
	requires java.net.http;
	requires com.google.gson;
	
	exports common;
	exports rmiserver;
}