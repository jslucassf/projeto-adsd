package simulador;


import eduni.simjava.*;

public class Simulator {

    public static void main(String[] args) {

      long semente = 42;

      Sim_system.initialise();

      Source source = new Source("Source", 50, 5, semente);

      ServidorAppFront servidorAppFront = new ServidorAppFront("ServidorAppFront", 8, 2, semente);

      ServidorLogin servidorLoginFacebook = new ServidorLogin("ServidorLoginFacebook", 15, 3, semente);
      ServidorLogin servidorLoginGoogle = new ServidorLogin("ServidorLoginGoogle", 15, 3, semente);
      
      ServidorAppApi servidorAppApi = new ServidorAppApi("ServidorAppApi", 5, 1, semente);      
      ServidorBancoDeDados servidorBancoDeDados = new ServidorBancoDeDados("ServidorBancoDeDados", 5, 1, semente);
      
      Sensor sensorLCC = new Sensor("SensorLCC", 6, 2, semente);
      Sensor sensorPonto = new Sensor("SensorPonto", 6, 2.5, semente);
      Sensor sensorLSD = new Sensor("SensorLSD", 5, 2.2, semente);
      Sensor sensorBiblioteca = new Sensor("SensorBiblioteca", 6, 1, semente);     
 
      // Connection 
      Sim_system.link_ports("Source", "Out", "ServidorAppFront", "In");
      Sim_system.link_ports("ServidorAppFront", "Out1", "ServidorLoginFacebook", "In");
      Sim_system.link_ports("ServidorAppFront", "Out2", "ServidorLoginGoogle", "In");
      Sim_system.link_ports("ServidorLoginGoogle", "Out", "ServidorAppFront", "InLogin");
      Sim_system.link_ports("ServidorLoginFacebook", "Out", "ServidorAppFront", "InLogin");
      
      Sim_system.link_ports("ServidorAppFront", "Out3", "ServidorAppApi", "In");
      
      Sim_system.link_ports("ServidorAppApi", "Out1", "SensorLCC", "In");
      Sim_system.link_ports("SensorLCC", "Out", "ServidorAppApi", "In1");
      
      Sim_system.link_ports("ServidorAppApi", "Out2", "SensorPonto", "In");
      Sim_system.link_ports("SensorPonto", "Out", "ServidorAppApi", "In2");

      Sim_system.link_ports("ServidorAppApi", "Out3", "SensorLSD", "In");
      Sim_system.link_ports("SensorLSD", "Out", "ServidorAppApi", "In3");
      
      Sim_system.link_ports("ServidorAppApi", "Out4", "SensorBiblioteca", "In");
      Sim_system.link_ports("SensorBiblioteca", "Out", "ServidorAppApi", "In4");
      
      Sim_system.link_ports("ServidorAppApi", "Out5", "ServidorBancoDeDados", "In");
      
      Sim_system.link_ports("ServidorBancoDeDados", "Out", "ServidorAppApi", "InBD");
      
      Sim_system.link_ports("ServidorAppApi", "OutFront", "ServidorAppFront", "InAPI");

      Sim_system.set_trace_detail(false, true, false);
      
      Sim_system.set_report_detail(true, false);
      
      Sim_system.run();
    }
  }