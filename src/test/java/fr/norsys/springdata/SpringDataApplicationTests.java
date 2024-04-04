package fr.norsys.springdata;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Testcontainers
@SpringBootTest
class SpringDataApplicationTests {

    @Autowired
    EntityManager entityManager;

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));


    @Nested
    class Generales {

        @Test
        @DisplayName("Pour commencer, vous désirez connaître le nombre de personnes que vous avez dans votre base de données")
        void question1() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT COUNT(*) FROM PEOPLE";
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (Long) typedQuery.getSingleResult();
            assertEquals(410, result);
        }

        @Test
        @DisplayName("Comment trouver l'email de la personne dont le nom de famille est 'Warren'")
        void question2() {
            // TODO : Ecrire la requête SQL
            var query = "select email from PEOPLE where lastname like 'Warren'";
            var expected = "aliquet.Phasellus@Nullamutnisi.org";
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (String) typedQuery.getSingleResult();
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("Comment trier les donnée de la table people par ordre alphabétique croissant sur le nom de famille ?")
        void question3() {
            // TODO : Ecrire la requête SQL
            var query = "select CONCAT(firstname,' ',lastname) from PEOPLE order by lastname asc";
            /** expected
             * first result : Macon Abbott
             * last result : Clark Zimmerman
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            // TODO: Vérifier le résultat
            assertEquals("Macon Abbott", result.get(0).toString());
            assertEquals("Clark Zimmerman",result.get(result.size()-1).toString());
        }

        @Test
        @DisplayName("Il y a-t-il un moyen de limiter le nombre de résultat, par exemple en affichant uniquement les 5 premiers, toujours triés par nom de famille ?")
        void question4() {
            // TODO : Ecrire la requête SQL
            var query = "select CONCAT(firstname,',',lastname) from PEOPLE order by lastname asc limit 5";
            /** expected
             * Caryn,Abbott
             * Macon,Abbott
             * Nichole,Acosta
             * Sharon,Adams
             * Ezekiel,Aguilar
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result =  typedQuery.getResultList();
            assertEquals(5, result.size());
            assertEquals("Caryn,Abbott",result.get(0));
            assertEquals("Ezekiel,Aguilar",result.get(4));
        }

        @Test
        @DisplayName("Comment trouver les personnes qui ont un prénom ou un nom qui contient ojo ?")
        void question5() {
            // TODO : Ecrire la requête SQL
            var query = "select CONCAT(firstname,',',lastname) from PEOPLE where firstname like '%ojo%' or lastname like '%ojo%'";
            /** expected
             Bruce,Cojote
             Chantale,Hallojo
             Shea,Nojoman
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            // TODO: Vérifier le résultat
            assertEquals("Bruce,Cojote", result.get(0).toString());
            assertEquals("Shea,Nojoman",result.get(result.size()-1));
        }

        @Test
        @DisplayName("Quelles sont les 5 personnes les plus jeunes ? Et les plus agées ?")
        void question6() {
            // TODO : Ecrire la requête SQL
            var query = "(select CONCAT(firstname,',',lastname) from PEOPLE order by birthdate desc limit 5)"+
                    " union (select CONCAT(firstname,',',lastname) from PEOPLE order by birthdate asc limit 5) ";
            /** expected
             * Colby,William
             * Vladimir,Levine
             * Burton,Small
             * Holly,Norman
             * Laith,Baxter
             *
             Levi,Nolan
             Wallace,Christensen
             Gabriel,Rivas
             Yvonne,Sweeney
             Kieran,Rocha
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            //System.out.println(result);
            // TODO: Vérifier le résultat
            assertEquals("Colby,William", result.get(0));
            assertEquals("Kieran,Rocha", result.get(result.size()-1));

        }

        @Test
        @DisplayName("Comment trouver l'age, en année, des personnes ?")
        void question7() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM birthdate) FROM PEOPLE";
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals(24,Integer.valueOf(result.get(0).toString()));
            assertEquals(41,Integer.valueOf(result.get(result.size()-1).toString()));

        }

        @Test
        @DisplayName("Comment peut-on trouver la moyenne d'age des personnes présentes dans la table ?")
        void question8() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT AVG(EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM birthdate)) FROM PEOPLE";
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getSingleResult();
            assertEquals(30.18780487804878, Double.valueOf(result.toString()));
        }

        @Test
        @DisplayName("Votre designer travail sur les cartes de membre et il a besoin de savoir quelle est la personne avec le plus long prénom et le plus long nom.")
        void question9() {
            // TODO : Ecrire la requête SQL
            var query = "(SELECT CONCAT(firstname,',',lastname) FROM PEOPLE ORDER BY LENGTH(firstname) DESC limit 1) UNION " +
                    "(SELECT CONCAT(firstname,',',lastname) FROM PEOPLE ORDER BY LENGTH(lastname) DESC limit 1 )";
            /** expected
             * Clementine,Carey
             * Wallace,Christensen
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result =typedQuery.getResultList();
            assertEquals("Clementine,Carey", result.get(0));
            assertEquals("Wallace,Christensen", result.get(1));

        }

        @Test
        @DisplayName("Ne sachant encore pas exactement la manière dont le layout des cartes de membres sera organisé, il aimerait également savoir qui sont les 3 personnes qui ont, mis ensemble, la pair nom + prénom la plus longue.")
        void question10() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT CONCAT(firstname,',',lastname) FROM PEOPLE ORDER BY LENGTH(firstname || lastname) DESC limit 3";
            /** expected
             * Wallace,Christensen
             * Cheyenne,Pennington
             * Isabelle,Singleton
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result =typedQuery.getResultList();
            assertEquals("Wallace,Christensen", result.get(0));
            assertEquals("Cheyenne,Pennington", result.get(1));
            assertEquals("Isabelle,Singleton", result.get(2));

        }

        @Test
        @DisplayName("Il y a-t-il des doublons dans la table people")
        void question11() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT CONCAT(firstname,',',lastname,',',COUNT(*)) FROM PEOPLE GROUP BY firstname,lastname having count(*)>1 ";
            /** expected
             * Mara,Rollins,2
             * Rahim,Nieves,2
             * Zahir,Warner,2
             * August,Singleton,2
             * Dennis,Kaufman,2
             * Forrest,Mckee,2
             * Sybill,Sparks,2
             * Mechelle,Craft,2
             * Kelly,Pennington,2
             * Darrel,Melendez,2
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals("Mara,Rollins,2", result.get(0).toString());
            assertEquals("Darrel,Melendez,2", result.get(result.size()-1).toString());

        }


    }

    @Nested
    class Inviation {

        @Test
        @DisplayName("Pour l'ouverture, vous désirez lister tous les membres de plus de 18 ans, et de moins de 60 ans, qui ont une addresse email valide")
        void question12() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT * FROM PEOPLE WHERE DATE_PART('year', AGE(CURRENT_DATE, birthdate)) > 18 " +
                    "AND DATE_PART('year', AGE(CURRENT_DATE, birthdate)) < 60 AND email like '%@%'";
            // expected : 404 records
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals(404, result.size());
        }

        @Test
        @DisplayName("Pour faciliter la lecture vous ajoutez une colonne age dans le résultat de votre requête")
        void question13() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM birthdate),* FROM PEOPLE WHERE DATE_PART('year', AGE(CURRENT_DATE, birthdate)) > 18 " +
                    "AND DATE_PART('year', AGE(CURRENT_DATE, birthdate)) < 60 AND email like '%@%'";
            /** expected
             * 404 records
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            // TODO : Vérifier le résultat
            assertEquals(404, result.size());
        }

        @Test
        @DisplayName("Avec ces membres, vous désirez faire une liste sous le format suivant Prénom Nom <email@provider.com>; afin de pouvoir la copier/coller dans votre client email")
        void question14() {
            // TODO : Ecrire la requête SQL
            var query = "select CONCAT(firstname,' ',lastname,' <',email,'>') FROM PEOPLE";
            /** expected
             * ex: Dawn Powell <nec@eueuismodac.com>
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            // TODO : Vérifier le résultat
            assertEquals("Dawn Powell <nec@eueuismodac.com>", result.get(0).toString());
        }

        @Test
        @DisplayName("Avec les informations contenues dans la table people (sans jointures), pourrait-on approximer le nombre de personnes habitant en Suisse ?")
        void question15() {
            // TODO : Ecrire la requête SQL
            var query = "select * from PEOPLE where email like '%.ch'";
            /** expected
             * 70
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            // TODO : Vérifier le résultat
            assertEquals(70, result.size());
        }
    }

    @Nested
    class Jointure {

        @Test
        @DisplayName("En utilisant la table de jointure countries_people.sql, lister les personnes habitant en Suisse")
        void question16() {
            // TODO : Ecrire la requête SQL
            var query = "select count(*) from countries_people where idcountry=756";
            /** expected
             * 371
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (Long) typedQuery.getSingleResult();
            assertEquals(371, result);
        }

        @Test
        @DisplayName("De la même manière, lister les personnes qui n'habitent pas en Suisse")
        void question17() {
            // TODO : Ecrire la requête SQL
            var query = "select * from PEOPLE where email not like '%.ch'";
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals(340, result.size());
        }

        @Test
        @DisplayName("comment lister les personnes (nom et prénom) qui habitent dans les pays limitrophe de la Suisse ? (i.e France, Allemagne, Italie, Autriche, Lischenchtein)")
        void question18() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT * FROM people p  JOIN countries c ON p.id = cp.idpeople JOIN countrie_people cp ON c.id = cp.idcountry"+
            " WHERE c.name_fr IN ('France', 'Allemagne', 'Italie', 'Autriche', 'Lischenchtein')";
            // expected : 30 records
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals(30, result.size());
        }

        @Test
        @DisplayName("Vous souhaitez savoir combien il y a de personnes par pays, afin de savoir si votre table people a suffisament de personnes en suisse et combien de personnes sont étrangères.")
        void question19() {
            // TODO : Ecrire la requête SQL
            var query = "";
            /**
             * VN,1
             * BA,1
             * DM,1
             * CH,371
             * SO,1
             * GB,3
             * IT,4
             * AT,6
             * LI,3
             * DE,5
             * SZ,1
             * TG,1
             * SY,1
             * BE,2
             * DZ,1
             * FR,12
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals(0, result.size());
        }

        @Test
        @DisplayName("Quels sont les pays qui ne possèdent pas de personnes")
        void question20() {
            // TODO : Ecrire la requête SQL
            var query = "";
            // expected : 232 records
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals(0, result.size());
        }

        @Test
        @DisplayName("Il y a-t-il des personnes qui sont liées à plusieurs pays ? Si oui, lesquelles ?")
        void question21() {
            // TODO : Ecrire la requête SQL
            var query = "";
            // expected : 12 records
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals(0, result.size());
        }

        @Test
        @DisplayName("Il y a-t-il des personnes liées à aucun pays ? Si oui, combien ?")
        void question22() {
            // TODO : Ecrire la requête SQL
            var query = "";
            // expected : 0
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals(0, result.size());
        }

        @Test
        @DisplayName("Comment pourrait-on afficher le pourcentage de personnes par pays")
        void question23() {
            // TODO : Ecrire la requête SQL
            var query = "";
            /**
             * Syrian Arab Republic,0.24%
             * Algeria,0.24%
             * Bosnia and Herzegovina,0.24%
             * Dominica,0.24%
             * Belgium,0.49%
             * United Kingdom,0.73%
             * Germany,1.22%
             * Swaziland,0.24%
             * Viet Nam,0.24%
             * France,2.93%
             * Togo,0.24%
             * Italy,0.98%
             * Liechtenstein,0.73%
             * Switzerland,90.49%
             * Austria,1.46%
             * Somalia,0.24%
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals(0, result.size());
        }
    }


}
