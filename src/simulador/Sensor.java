package simulador;


import eduni.simjava.*;
import eduni.simjava.distributions.*;

class Sensor extends Sim_entity {
  private Sim_port in, out;
  private Sim_normal_obj delay;
  private Sim_stat stat;

  Sensor(String name, double media, double variancia, long semente) {
    super(name);
    this.delay = new Sim_normal_obj("Delay", media, variancia, semente);
    
    add_generator(this.delay);

    in = new Sim_port("In");
    out = new Sim_port("Out");
    
    add_port(in);
    add_port(out);
    
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
    while (Sim_system.running()) {
      Sim_event e = new Sim_event();

      sim_get_next(e);
      double tempoEspera = delay.sample();
      sim_trace(1, "Sensor recebeu uma requisição. Delay: "+ tempoEspera);

      sim_process(tempoEspera);

      sim_completed(e);
      
  	  sim_trace(1, "Enviando resposta para o Servidor de Aplicação API");
  	  sim_schedule(out, 0.0, 1);

    }
  }
}