package simulador;

import eduni.simjava.*;
import eduni.simjava.distributions.*;

class ServidorAppFront extends Sim_entity {
  private Sim_port in, inLogin, out1, out2, out3, inAPI;
  private Sim_normal_obj delay;
  private Sim_random_obj prob;
  private Sim_stat stat;

  ServidorAppFront(String name, double media, double variancia, long semente) {
    super(name);
    this.delay = new Sim_normal_obj("Delay", media, variancia, semente);
    this.prob = new Sim_random_obj("Probabilidade", semente);
    
    add_generator(this.delay);
    add_generator(this.prob);
    
    in = new Sim_port("In");
    inLogin = new Sim_port("InLogin");
    inAPI = new Sim_port("InAPI");
    out1 = new Sim_port("Out1");
    out2 = new Sim_port("Out2");
    out3 = new Sim_port("Out3");
    
    add_port(in);
    add_port(inLogin);
    add_port(inAPI);
    add_port(out1); // Saída para servidor de login do Facebook
    add_port(out2); // Saída para servidor de login do Google
    add_port(out3); // Saída para servidor de API
    
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
	  sim_trace(1, "Servidor de Aplicação Front iniciou. Delay: " + tempoEspera);

      sim_process(tempoEspera);
      sim_completed(e);
      
      try {
    	  String nome = Sim_system.get_entity(e.get_src()).get_name();
    	  if (nome.equals("ServidorLoginFacebook") || nome.equals("ServidorLoginGoogle")) {
    		  sim_trace(1, "Enviando requisição para Servidor de Aplicação API");
    		  sim_schedule(out3, 0.0, 1);    	  
          } else {
        	  double probLogin = prob.sample();
        	  
        	  if (probLogin < 0.5) {
        	    	sim_trace(1, "Servidor de login Facebook selecionado");
        	    	sim_schedule(out1, 0.0, 1);
        	      } else {
        	    	sim_trace(1, "Servidor de Login Google selecionado");
        	    	sim_schedule(out2, 0.0, 1);
        	      }
        	      i++;        	  
          }    	      	  
      } catch (eduni.simjava.Sim_exception exc) {
    	  System.out.println(exc.getMessage());
      }
      
    }
  }
}