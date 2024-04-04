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

import java.util.List;

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
            var query = "select count(p.id) from people p";
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (Long) typedQuery.getSingleResult();
            assertEquals(410, result);
        }

        @Test
        @DisplayName("Comment trouver l'email de la personne dont le nom de famille est 'Warren'")
        void question2() {
            // TODO : Ecrire la requête SQL
            var query = "select p.email from people p where p.lastname = 'Warren'";
            var expected = "aliquet.Phasellus@Nullamutnisi.org";
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (String) typedQuery.getSingleResult();
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("Comment trier les donnée de la table people par ordre alphabétique croissant sur le nom de famille ?")
        void question3() {
            // TODO : Ecrire la requête SQL
            var query = "select p.lastname from people p order by p.lastname ASC limit 1";
            /** expected
             * first result : Macon Abbott
             * last result : Clark Zimmerman
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (String) typedQuery.getSingleResult();
            // TODO: Vérifier le résultat
            assertEquals("Abbott", result);
        }

        @Test
        @DisplayName("Il y a-t-il un moyen de limiter le nombre de résultat, par exemple en affichant uniquement les 5 premiers, toujours triés par nom de famille ?")
        void question4() {
            // TODO : Ecrire la requête SQL
            var query = "select concat(p.firstname, ',', p.lastname) AS fullname from people" +
                    " p order by  p.lastname ASC limit 5";
            /** expected
             * Caryn,Abbott
             * Macon,Abbott
             * Nichole,Acosta
             * Sharon,Adams
             * Ezekiel,Aguilar
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (List<String>) typedQuery.getResultList();
            assertEquals("Caryn,Abbott", result.get(0));
            assertEquals("Macon,Abbott", result.get(1));
            assertEquals("Nichole,Acosta", result.get(2));
            assertEquals("Sharon,Adams", result.get(3));
            assertEquals("Ezekiel,Aguilar", result.get(4));
        }

        @Test
        @DisplayName("Comment trouver les personnes qui ont un prénom ou un nom qui contient ojo ?")
        void question5() {
            // TODO : Ecrire la requête SQL
            var query = "select concat(p.firstname, ',', p.lastname) AS fullname from people p " +
                    "where p.firstname like '%ojo%'  or p.lastname like '%ojo%'  " +
                    "order by  p.lastname";
            /** expected
             Bruce,Cojote
             Chantale,Hallojo
             Shea,Nojoman
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (List<String>) typedQuery.getResultList();
            // TODO: Vérifier le résultat
            assertEquals("Bruce,Cojote", result.get(0));
            assertEquals("Chantale,Hallojo", result.get(1));
            assertEquals("Shea,Nojoman", result.get(2));
        }


        @Test
        @DisplayName("Quelles sont les 5 personnes les plus jeunes ? Et les plus agées ?")
        void question6() {
            // TODO : Ecrire la requête SQL
            var query = "(select concat(p.firstname, ',', p.lastname) as fullname from people p " +
                    "order by p.birthdate ASC LIMIT 5 )" +
                    "UNION ALL " +
                    "(select CONCAT(p.firstname, ',', p.lastname) AS fullname FROM people p " +
                    "order by p.birthdate DESC LIMIT 5)";

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
            var result = (List<String>) typedQuery.getResultList();
            // TODO: Vérifier le résultat

            assertEquals("Colby,William", result.get(0));
            assertEquals("Vladimir,Levine", result.get(1));
            assertEquals("Burton,Small", result.get(2));
            assertEquals("Holly,Norman", result.get(3));
            assertEquals("Laith,Baxter", result.get(4));

            assertEquals("Levi,Nolan", result.get(5));
            assertEquals("Wallace,Christensen", result.get(6));
            assertEquals("Gabriel,Rivas", result.get(7));
            assertEquals("Yvonne,Sweeney", result.get(8));
            assertEquals("Kieran,Rocha", result.get(9));
        }

        @Test
        @DisplayName("Comment trouver l'age, en année, des personnes ?")
        void question7() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT DATE_PART('YEAR', AGE(CURRENT_DATE, p.birthdate)) AS age from people p";
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (List<Double>) typedQuery.getResultList();
            // TODO: Vérifier le résultat

            assertEquals(23.0, result.get(0));
            assertEquals(33.0, result.get(1));
            assertEquals(24.0, result.get(2));
            assertEquals(23.0, result.get(3));

        }

        @Test
        @DisplayName("Comment peut-on trouver la moyenne d'age des personnes présentes dans la table ?")
        void question8() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT AVG(DATE_PART('YEAR', AGE(CURRENT_DATE, p.birthdate))) AS avgAge from people p";
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (Double) typedQuery.getSingleResult();
            assertEquals(29.39512195121951, result);
        }

        @Test
        @DisplayName("Votre designer travail sur les cartes de membre et il a besoin de savoir quelle est la personne avec le plus long prénom et le plus long nom.")
        void question9() {
            // TODO : Ecrire la requête SQL
            var query = "select concat(p.firstname, ',', p.lastname) as fullname from people p" +
                    " order by length(p.firstname) DESC ,length(p.firstname) DESC " +
                    "Limit 2";
            /** expected
             * Clementine,Carey
             * Wallace,Christensen
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (List<String>) typedQuery.getResultList();

            assertEquals("Wallace,Christensen", result.get(0));
            assertEquals("Cheyenne,Pennington", result.get(1));
        }

        @Test
        @DisplayName("Ne sachant encore pas exactement la manière dont le layout des cartes de membres sera organisé, il aimerait également savoir qui sont les 3 personnes qui ont, mis ensemble, la pair nom + prénom la plus longue.")
        void question10() {
            // TODO : Ecrire la requête SQL
            var query = "select concat(p.firstname, ',', p.lastname) as fullname from people p" +
                    " order by length(concat(p.firstname, ',', p.lastname)) DESC " +
                    "Limit 3";
            /** expected
             * Wallace,Christensen
             * Cheyenne,Pennington
             * Isabelle,Singleton
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (List<String>) typedQuery.getResultList();

            assertEquals("Wallace,Christensen", result.get(0));
            assertEquals("Cheyenne,Pennington", result.get(1));
            assertEquals("Isabelle,Singleton", result.get(1));
        }

        @Test
        @DisplayName("Il y a-t-il des doublons dans la table people")
        void question11() {
            // TODO : Ecrire la requête SQL
            var query = "select p.firstname , p.lastname ,count(p.id) from people p  group by p.firstname,p.lastname having count(p.id)>1 order by p.lastname asc";

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
            var result = (List<String>) typedQuery.getResultList();

            assertEquals(10, result.size());

        }


    }

    @Nested
    class Inviation {

        @Test
        @DisplayName("Pour l'ouverture, vous désirez lister tous les membres de plus de 18 ans, et de moins de 60 ans, qui ont une addresse email valide")
        void question12() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT * from people p where p.email like '%@%' and DATE_PART('YEAR', AGE(CURRENT_DATE, p.birthdate)) between 18 and 60";
            // expected : 404 records
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (Long) typedQuery.getSingleResult();
            assertEquals(404, result);
        }

        @Test
        @DisplayName("Pour faciliter la lecture vous ajoutez une colonne age dans le résultat de votre requête")
        void question13() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT p.*, DATE_PART('YEAR', AGE(CURRENT_DATE, p.birthdate)) AS age from people p where p.email like '%@%' and DATE_PART('YEAR', AGE(CURRENT_DATE, p.birthdate)) between 18 and 60";
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
            var query = "SELECT CONCAT(p.firstname, ' ', p.lastname, ' <', p.email, '>') AS membre  from people p where p.email like '%@%' and DATE_PART('YEAR', AGE(CURRENT_DATE, p.birthdate)) between 18 and 60";
            /** expected
             * ex: Dawn Powell <nec@eueuismodac.com>
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            // TODO : Vérifier le résultat
            assertEquals(404, result.size());
        }

        @Test
        @DisplayName("Avec les informations contenues dans la table people (sans jointures), pourrait-on approximer le nombre de personnes habitant en Suisse ?")
        void question15() {
            // TODO : Ecrire la requête SQL
            var query = "select count(*) from people p where p.id in (" +
                    "(select cp.idperson from countries_people cp where cp.idcountry in (" +
                    "select c.id from countries c where c.name_fr ='Suisse')))";
            /** expected
             * 70
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (Long) typedQuery.getSingleResult();
            // TODO : Vérifier le résultat
            assertEquals(371, result);
        }
    }

    @Nested
    class Jointure {

        @Test
        @DisplayName("En utilisant la table de jointure countries_people.sql, lister les personnes habitant en Suisse")
        void question16() {
            // TODO : Ecrire la requête SQL
            var query = "select COUNT(*) " +
                    "from people p " +
                    "         join countries_people cp on p.id = cp.idperson " +
                    "         join countries c on cp.idcountry = c.id " +
                    "where c.name_fr = 'Suisse' ";
            /** expected
             * 371
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (Long) typedQuery.getSingleResult();
            assertEquals(0, result);
        }

        @Test
        @DisplayName("De la même manière, lister les personnes qui n'habitent pas en Suisse")
        void question17() {
            // TODO : Ecrire la requête SQL
            var query = "select COUNT(*) " +
                    "from people p " +
                    "         join countries_people cp on p.id = cp.idperson " +
                    "         join countries c on cp.idcountry = c.id " +
                    "where c.name_fr != 'Suisse' ";
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (Long) typedQuery.getSingleResult();
            assertEquals(43, result);
        }

        @Test
        @DisplayName("comment lister les personnes (nom et prénom) qui habitent dans les pays limitrophe de la Suisse ? (i.e France, Allemagne, Italie, Autriche, Lischenchtein)")
        void question18() {
            // TODO : Ecrire la requête SQL
            var query = "select CONCAT(p.firstname, ' ', p.lastname) as fullname " +
                    "from people p " +
                    "         join countries_people cp on p.id = cp.idperson " +
                    "         join countries c on cp.idcountry = c.id " +
                    "where c.name_fr IN ('France', 'Allemagne', 'Italie', 'Autriche', 'Liechtenstein')";
            // expected : 30 records
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals(30, result.size());
        }

        @Test
        @DisplayName("Vous souhaitez savoir combien il y a de personnes par pays, afin de savoir si votre table people a suffisament de personnes en suisse et combien de personnes sont étrangères.")
        void question19() {
            // TODO : Ecrire la requête SQL
            var query = "select c.iso2, COUNT(*) as count " +
                    "from people p " +
                    "         join countries_people cp ON p.id = cp.idperson " +
                    "         join countries c ON cp.idcountry = c.id " +
                    "group by c.iso2 ";
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
            assertEquals(16, result.size());
        }

        @Test
        @DisplayName("Quels sont les pays qui ne possèdent pas de personnes")
        void question20() {
            // TODO : Ecrire la requête SQL
            var query = "select * from countries c\n" +
                    "where c.id not in  (select cp.idcountry from countries_people cp)";
            // expected : 232 records
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals(232, result.size());
        }

        @Test
        @DisplayName("Il y a-t-il des personnes qui sont liées à plusieurs pays ? Si oui, lesquelles ?")
        void question21() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT COUNT(*) AS country_count " +
                    "FROM people p " +
                    "         JOIN countries_people cp ON p.id = cp.idperson " +
                    "GROUP BY p.firstname ,p.lastname " +
                    "HAVING COUNT(cp.idcountry) > 1; ";
            // expected : 12 records
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals(12, result.size());
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
