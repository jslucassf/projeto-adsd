package simulador;


import eduni.simjava.*;
import eduni.simjava.distributions.*;

class ServidorAppApi extends Sim_entity {
  private Sim_port in, in1, in2, in3, in4, out1, out2, out3, out4, out5, inBD, outFront;
  private Sim_normal_obj delay;
  private Sim_random_obj prob;
  private Sim_stat stat;

  ServidorAppApi(String name, double media, double variancia, long semente) {
    super(name);
    this.delay = new Sim_normal_obj("Delay", media, variancia, semente);
    this.prob = new Sim_random_obj("Probabilidade", semente);
    
    add_generator(this.delay);
    add_generator(this.prob);
    
    in = new Sim_port("In");
    inBD = new Sim_port("InBD");
    in1 = new Sim_port("In1");
    in2 = new Sim_port("In2");
    in3 = new Sim_port("In3");
    in4 = new Sim_port("In4");
    out1 = new Sim_port("Out1");
    out2 = new Sim_port("Out2");
    out3 = new Sim_port("Out3");
    out4 = new Sim_port("Out4");
    out5 = new Sim_port("Out5");
    outFront = new Sim_port("OutFront");
    
    
    add_port(in);
    add_port(inBD);
    add_port(in1);
    add_port(in2);
    add_port(in3);
    add_port(in4);
    add_port(out1);
    add_port(out2);
    add_port(out3);
    add_port(out4);
    add_port(out5);
    add_port(outFront);
    
    stat = new Sim_stat();
    stat.add_measure(Sim_stat.ARRIVAL_RATE);        
    stat.add_measure(Sim_stat.QUEUE_LENGTH);    
    stat.add_measure(Sim_stat.RESIDENCE_TIME);
    stat.add_measure(Sim_stat.SERVICE_TIME);    
    stat.add_measure(Sim_stat.THROUGHPUT);
    stat.add_measure(Sim_stat.UTILISATION);
    stat.add_measure(Sim_stat.WAITING_TIME);    
    set_stat(stat);  
    
  }

  public void body() {
	int i = 0;
    while (Sim_system.running()) {
      Sim_event e = new Sim_event();

      sim_get_next(e);
      double tempoEspera = delay.sample();
      sim_trace(1, "Servidor de Aplicação API iniciou. Delay: " + tempoEspera);

      sim_process(tempoEspera);

      sim_completed(e);
      try {
    	  String nome = Sim_system.get_entity(e.get_src()).get_name();
    	  if (nome.equals("ServidorAppFront")) {
        	  double probSensor = prob.sample();
        	  
    		  if (probSensor < 0.25) {
      	    	sim_trace(1, "Requisição Enviada para o Sensor 1");
      	    	sim_schedule(out1, 0.0, 1);
      	      } else if (probSensor < 0.5) {
      	    	sim_trace(1, "Requisição Enviada para o Sensor 2");
      	    	sim_schedule(out2, 0.0, 1);  
      	      } else if (probSensor < 0.75) {
      	    	sim_trace(1, "Requisição Enviada para o Sensor 3");
      	    	sim_schedule(out3, 0.0, 1);
      	      } else {
      	    	sim_trace(1, "Requisição Enviada para o Sensor 4");
      	    	sim_schedule(out4, 0.0, 1);
      	      }
      	      i++;    	  
          } else if(nome.equals("ServidorBancoDeDados")) {
        	  sim_trace(1, "Enviando resposta para Servidor de Front End");
    		  sim_schedule(outFront, 0.0, 1);    
          } 
    	  else {
    		  sim_trace(1, "Enviando query para Servidor de Banco de Dados");
    		  sim_schedule(out5, 0.0, 1);
          }    	      	  
      } catch (eduni.simjava.Sim_exception exc) {
    	  System.out.println(exc.getMessage());
      }

    }
  }
}