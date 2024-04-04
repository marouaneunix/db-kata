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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

@Testcontainers
@SpringBootTest
class SpringDataApplicationTests {

    @Autowired
    EntityManager entityManager;

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:latest"));

    @Nested
    class Generales {

        @Test
        @DisplayName("Pour commencer, vous désirez connaître le nombre de personnes que vous avez dans votre base de données")
        void question1() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT COUNT(*) FROM people";
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (Long) typedQuery.getSingleResult();
            assertEquals(410, result);
        }

        @Test
        @DisplayName("Comment trouver l'email de la personne dont le nom de famille est 'Warren'")
        void question2() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT email FROM people WHERE lastname = 'Warren'";
            var expected = "aliquet.Phasellus@Nullamutnisi.org";
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (String) typedQuery.getSingleResult();
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("Comment trier les donnée de la table people par ordre alphabétique croissant sur le nom de famille ?")
        void question3() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT CONCAT(firstname, ' ', lastname) AS full_name FROM people ORDER BY lastname ASC";
            /**
             * expected
             * first result : Macon Abbott
             * last result : Clark Zimmerman
             */
            var typedQuery = entityManager.createNativeQuery(query);
            List<String> resultList = typedQuery.getResultList();
            // TODO: Vérifier le résultat

            String firstFullName = resultList.get(0);
            String lastFullName = resultList.get(resultList.size() - 1);

            String expectedFirstFullName = "Macon Abbott";
            String expectedLastFullName = "Clark Zimmerman";

            assertEquals(expectedFirstFullName, firstFullName);
            assertEquals(expectedLastFullName, lastFullName);
        }

        @Test
        @DisplayName("Il y a-t-il un moyen de limiter le nombre de résultat, par exemple en affichant uniquement les 5 premiers, toujours triés par nom de famille ?")
        void question4() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT CONCAT(firstname, ',', lastname) AS full_name " +
                    "FROM people " +
                    "ORDER BY lastname ASC " +
                    "LIMIT 5";
            /**
             * expected
             * Caryn,Abbott
             * Macon,Abbott
             * Nichole,Acosta
             * Sharon,Adams
             * Ezekiel,Aguilar
             */
            var typedQuery = entityManager.createNativeQuery(query);
            List<String> resultList = typedQuery.getResultList();
            List<String> expectedList = new ArrayList<>();
            expectedList.add("Caryn,Abbott");
            expectedList.add("Macon,Abbott");
            expectedList.add("Nichole,Acosta");
            expectedList.add("Sharon,Adams");
            expectedList.add("Ezekiel,Aguilar");

            // assert
            assertEquals(expectedList, resultList);
        }

        @Test
        @DisplayName("Comment trouver les personnes qui ont un prénom ou un nom qui contient ojo ?")
        void question5() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT CONCAT(firstname, ',', lastname) AS full_name " +
                    "FROM people " +
                    "WHERE firstname LIKE '%ojo%' OR lastname LIKE '%ojo%' " +
                    "ORDER BY lastname ASC";
            /**
             * expected
             * Bruce,Cojote
             * Chantale,Hallojo
             * Shea,Nojoman
             */
            var typedQuery = entityManager.createNativeQuery(query);
            List<String> resultList = typedQuery.getResultList();
            // TODO: Vérifier le résultat
            assertEquals(3, resultList.size());
            assertEquals("Bruce,Cojote", resultList.get(0));
            assertEquals("Shea,Nojoman", resultList.get(2));
        }

        @Test
        @DisplayName("Quelles sont les 5 personnes les plus jeunes ? Et les plus agées ?")
        void question6() {
            // TODO : Ecrire la requête SQL
            var query = "(SELECT CONCAT(firstname, ',', lastname) AS full_name " +
                    "FROM people " +
                    "ORDER BY birthdate ASC " +
                    "LIMIT 5)" +
                    "UNION ALL " +
                    "(SELECT CONCAT(firstname, ',', lastname) AS full_name " +
                    "FROM people " +
                    "ORDER BY birthdate DESC " +
                    "LIMIT 5)";
            /**
             * expected
             * Colby,William
             * Vladimir,Levine
             * Burton,Small
             * Holly,Norman
             * Laith,Baxter
             *
             * Levi,Nolan
             * Wallace,Christensen
             * Gabriel,Rivas
             * Yvonne,Sweeney
             * Kieran,Rocha
             */
            var typedQuery = entityManager.createNativeQuery(query);
            List<String> resultList = typedQuery.getResultList();
            // TODO: Vérifier le résultat
            assertEquals(10, resultList.size());
            assertEquals("Colby,William", resultList.get(0));
            assertEquals("Kieran,Rocha", resultList.get(9));
        }

        @Test
        @DisplayName("Comment trouver l'age, en année, des personnes ?")
        void question7() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT CONCAT(firstname, ',', lastname) AS full_name, " +
                    "DATE_PART('year', AGE(birthdate)) AS age " +
                    "FROM people " +
                    "ORDER BY age DESC";
            var typedQuery = entityManager.createNativeQuery(query);
            List<Object[]> resultList = typedQuery.getResultList();

            String expectedFirstFullName = "Colby,William";
            double expectedAge = 99;

            long negativeAgeCount = resultList.stream()
                    .filter(objects -> ((Double) objects[1]).intValue() < 0)
                    .count();

            // assert
            assertEquals(410, resultList.size());
            assertEquals(0, negativeAgeCount);
            assertEquals(expectedFirstFullName, resultList.get(0)[0]);
            assertEquals(expectedAge, resultList.get(0)[1]);
        }

        @Test
        @DisplayName("Comment peut-on trouver la moyenne d'age des personnes présentes dans la table ?")
        void question8() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT AVG(DATE_PART('year', AGE(birthdate))) FROM people";
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals(29.39512195121951, result.get(0));
        }

        @Test
        @DisplayName("Votre designer travail sur les cartes de membre et il a besoin de savoir quelle est la personne avec le plus long prénom et le plus long nom.")
        void question9() {
            // TODO : Ecrire la requête SQL
            var query = "(SELECT CONCAT(firstname, ',', lastname) AS full_name " +
                    "FROM people " +
                    "ORDER BY LENGTH(firstname) DESC, LENGTH(lastname) DESC " +
                    "LIMIT 1)" +
                    "UNION ALL " +
                    "(SELECT CONCAT(firstname, ',', lastname) AS full_name " +
                    "FROM people " +
                    "ORDER BY LENGTH(lastname) DESC, LENGTH(firstname) DESC " +
                    "LIMIT 1)";
            /**
             * expected
             * Clementine,Carey
             * Wallace,Christensen
             */
            var typedQuery = entityManager.createNativeQuery(query);
            List<String> resultList = typedQuery.getResultList();
            String expectedFirstFullName = "Clementine,Carey";
            String expectedSecondFullName = "Wallace,Christensen";
            // assert
            assertEquals(2, resultList.size());
            assertEquals(expectedFirstFullName, resultList.get(0));
            assertEquals(expectedSecondFullName, resultList.get(1));
        }

        @Test
        @DisplayName("Ne sachant encore pas exactement la manière dont le layout des cartes de membres sera organisé, il aimerait également savoir qui sont les 3 personnes qui ont, mis ensemble, la pair nom + prénom la plus longue.")
        void question10() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT concat(firstname ,',',lastname) FROM people ORDER BY length(firstname) + length(lastname) DESC LIMIT 3";
            /**
             * expected
             * Wallace,Christensen
             * Cheyenne,Pennington
             * Isabelle,Singleton
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals("Wallace,Christensen", result.get(0));
            assertEquals("Cheyenne,Pennington", result.get(1));
            assertEquals("Isabelle,Singleton", result.get(2));
        }

        @Test
        @DisplayName("Il y a-t-il des doublons dans la table people")
        void question11() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT firstname, lastname, COUNT(*) " +
                    "FROM people " +
                    "GROUP BY firstname, lastname " +
                    "HAVING COUNT(*) > 1";
            /**
             * expected
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

            Object[] firstRecord = (Object[]) result.get(0);
            String firstName = (String) firstRecord[0];
            String lastName = (String) firstRecord[1];
            long count = ((Number) firstRecord[2]).longValue();

            assertTrue(result.size() == 10);

            assertEquals("Mara", firstName);
            assertEquals("Rollins", lastName);
            assertEquals(2, count);
        }

    }

    @Nested
    class Inviation {

        @Test
        @DisplayName("Pour l'ouverture, vous désirez lister tous les membres de plus de 18 ans, et de moins de 60 ans, qui ont une addresse email valide")
        void question12() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT * FROM people WHERE DATE_PART('year', AGE(birthdate)) > 18 AND DATE_PART('year', AGE(birthdate)) < 60 AND email like '%@%' ";
            // expected : 404 records
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList().size();
            assertEquals(404, result);
        }

        @Test
        @DisplayName("Pour faciliter la lecture vous ajoutez une colonne age dans le résultat de votre requête")
        void question13() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT CONCAT(firstname, ',', lastname) AS full_name, " +
                    "DATE_PART('year', AGE(birthdate)) AS age, " +
                    "email " +
                    "FROM people " +
                    "WHERE DATE_PART('year', AGE(birthdate)) > 18 AND DATE_PART('year', AGE(birthdate)) < 60 AND email LIKE '%@%'";
            /**
             * expected
             * 404 records
             */

            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            Object[] firstRecord = (Object[]) result.get(2);
            // TODO : Vérifier le résultat
            assertEquals(404, result.size());
            assertEquals(3, firstRecord.length);

        }

        @Test
        @DisplayName("Avec ces membres, vous désirez faire une liste sous le format suivant Prénom Nom <email@provider.com>; afin de pouvoir la copier/coller dans votre client email")
        void question14() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT concat(firstname ,' ',lastname,' <',email,'>') FROM people WHERE DATE_PART('year', AGE(birthdate)) > 18 AND DATE_PART('year', AGE(birthdate)) < 60";
            /**
             * expected
             * ex: Dawn Powell <nec@eueuismodac.com>
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            // TODO : Vérifier le résultat
            assertEquals("Dawn Powell <nec@eueuismodac.com>", result.get(0));
        }

        @Test
        @DisplayName("Avec les informations contenues dans la table people (sans jointures), pourrait-on approximer le nombre de personnes habitant en Suisse ?")
        void question15() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT COUNT(*)\n" +
                    "FROM people\n" +
                    "WHERE id IN (SELECT idperson FROM countries_people WHERE idcountry = 756);";
            /**
             * expected
             * 70
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            // TODO : Vérifier le résultat
            assertEquals("371", result.get(0).toString());
        }
    }

    @Nested
    class Jointure {

        @Test
        @DisplayName("En utilisant la table de jointure countries_people.sql, lister les personnes habitant en Suisse")
        void question16() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT count(*) " +
                    "FROM countries_people cp " +
                    "JOIN countries c ON cp.idcountry = c.id " +
                    "JOIN people p ON cp.idperson = p.id " +
                    "WHERE c.name_fr = 'Suisse'";
            /**
             * expected
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
            var query = "SELECT count(*) " +
                    "FROM countries_people cp " +
                    "JOIN countries c ON cp.idcountry = c.id " +
                    "JOIN people p ON cp.idperson = p.id " +
                    "WHERE c.name_fr != 'Suisse'";
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();

            assertEquals("43", result.get(0).toString());
        }

        @Test
        @DisplayName("comment lister les personnes (nom et prénom) qui habitent dans les pays limitrophe de la Suisse ? (i.e France, Allemagne, Italie, Autriche, Lischenchtein)")
        void question18() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT firstname, lastname\n" +
                    "FROM people\n" +
                    "JOIN countries_people ON people.id = countries_people.idperson\n" +
                    "JOIN countries ON countries_people.idcountry = countries.id\n" +
                    "WHERE countries.name_en IN ('France', 'Germany', 'Italy', 'Austria', 'Liechtenstein');";
            // expected : 30 records
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals(30, result.size());
        }

        @Test
        @DisplayName("Vous souhaitez savoir combien il y a de personnes par pays, afin de savoir si votre table people a suffisament de personnes en suisse et combien de personnes sont étrangères.")
        void question19() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT c.iso2, COUNT(*) " +
                    "FROM countries_people cp " +
                    "JOIN countries c ON cp.idcountry = c.id " +
                    "GROUP BY c.iso2";

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
            var query = "SELECT c.name_fr FROM countries c LEFT JOIN countries_people cp ON c.id = cp.idcountry WHERE cp.idcountry IS NULL";
            // expected : 232 records
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals(232, result.size());
        }

        @Test
        @DisplayName("Il y a-t-il des personnes qui sont liées à plusieurs pays ? Si oui, lesquelles ?")
        void question21() {
            // TODO : Ecrire la requête SQL
            var query = "SELECT COUNT(*) " +
                    "FROM people p " +
                    "JOIN countries_people cp ON p.id = cp.idperson " +
                    "GROUP BY p.firstname, p.lastname " +
                    "HAVING COUNT(*) > 1";
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
