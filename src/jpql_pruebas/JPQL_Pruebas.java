/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpql_pruebas;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPQL_Pruebas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPQL_PruebasPU");
        EntityManager em = emf.createEntityManager();
        ValorJpaController controller = new ValorJpaController(emf);
        List<Valor> findValorEntities = controller.findValorEntities();
        System.out.println("Apellidos");
        for (Valor findValorEntity : findValorEntities) {

            System.out.println(findValorEntity.getApellido());
        }

       
        controller.create(new Valor("otto", "Ploter"));

    }

}
