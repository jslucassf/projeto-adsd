package simulador;

import eduni.simjava.*; // Import the SimJava basic package
import eduni.simjava.distributions.*;

class Source extends Sim_entity {
	private Sim_port out;
    private double media;
    private Sim_normal_obj delay;

    Source(String nome, double media, double variancia, long semente) {
      super(nome);
      this.delay = new Sim_normal_obj("Delay", media, variancia, semente);

      add_generator(this.delay);
      
      out = new Sim_port("Out");
      add_port(out);
    }
  
  public void body() {
      for (int i=0; i < 100; i++) {

        sim_schedule(out, 0.0, 0);
        
        double tempoEspera = delay.sample();
        
        sim_trace(1, "Nova requisição gerada para o Servidor de aplicação front. Delay: " + tempoEspera);
        
        sim_pause(tempoEspera);
      }
    }
}