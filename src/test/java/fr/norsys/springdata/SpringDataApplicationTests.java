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
            var query = "select count(*) from people";
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (Long) typedQuery.getSingleResult();
            assertEquals(410, result);
        }

        @Test
        @DisplayName("Comment trouver l'email de la personne dont le nom de famille est 'Warren'")
        void question2() {
            // TODO : Ecrire la requête SQL
            var query = """
                    select email from people
                        where lastname = 'Warren'
                    """;
            var expected = "aliquet.Phasellus@Nullamutnisi.org";
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (String) typedQuery.getSingleResult();
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("Comment trier les donnée de la table people par ordre alphabétique croissant sur le nom de famille ?")
        void question3() {
            // TODO : Ecrire la requête SQL
            var query = """
                    select concat(firstname, ' ', lastname) as fullName from people
                        order by lastname asc
                    """;
            /** expected
             * first result : Macon Abbott
             * last result : Clark Zimmerman
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            // TODO: Vérifier le résultat
            assertEquals("Macon Abbott", result.getFirst());
            assertEquals("Clark Zimmerman", result.getLast());
        }

        @Test
        @DisplayName("Il y a-t-il un moyen de limiter le nombre de résultat, par exemple en affichant uniquement les 5 premiers, toujours triés par nom de famille ?")
        void question4() {
            // TODO : Ecrire la requête SQL
            var query = """
                    select concat(firstname, ',', lastname) as fullName from people
                        order by lastname
                        limit 5
                    """;
            /** expected
             * Caryn,Abbott
             * Macon,Abbott
             * Nichole,Acosta
             * Sharon,Adams
             * Ezekiel,Aguilar
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals("Caryn,Abbott", result.get(0));
            assertEquals("Macon,Abbott", result.get(1));
            assertEquals("Nichole,Acosta", result.get(2));
            assertEquals("Sharon,Adams", result.get(3));
            assertEquals("Ezekiel,Aguilar", result.get(4));
            assertEquals(5, result.size());
        }

        @Test
        @DisplayName("Comment trouver les personnes qui ont un prénom ou un nom qui contient ojo ?")
        void question5() {
            // TODO : Ecrire la requête SQL
            var query = """
                    select concat(firstname, ',', lastname) as fullName
                    from people
                    where firstname like '%ojo%'
                       or lastname like '%ojo%'
                    """;
            /** expected
             Bruce,Cojote
             Chantale,Hallojo
             Shea,Nojoman
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            // TODO: Vérifier le résultat
            assertEquals("Bruce,Cojote", result.get(0));
            assertEquals("Chantale,Hallojo", result.get(1));
            assertEquals("Shea,Nojoman", result.get(2));
        }

        @Test
        @DisplayName("Quelles sont les 5 personnes les plus jeunes ? Et les plus agées ?")
        void question6() {
            // TODO : Ecrire la requête SQL
            var query = """
                    (select concat(firstname, ',', lastname)
                     from people
                     order by birthdate
                     limit 5)
                    union all
                    (select concat(firstname, ',', lastname)
                     from people
                     order by birthdate desc
                     limit 5)
                    """;
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
            var query = """
                    select DATE_PART('YEAR', AGE(CURRENT_DATE, birthdate)) from people
                    """;
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList().getFirst().toString();
            assertEquals("23.0", result);
        }

        @Test
        @DisplayName("Comment peut-on trouver la moyenne d'age des personnes présentes dans la table ?")
        void question8() {
            // TODO : Ecrire la requête SQL
            var query = """
                    select avg(DATE_PART('YEAR', AGE(CURRENT_DATE, birthdate))) from people
                    """;
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getSingleResult().toString();
            assertEquals("29.39512195121951", result);
        }

        @Test
        @DisplayName("Votre designer travail sur les cartes de membre et il a besoin de savoir quelle est la personne avec le plus long prénom et le plus long nom.")
        void question9() {
            // TODO : Ecrire la requête SQL
            var query = """
                    select concat(firstname, ',', lastname)
                    from people
                    order by length(firstname) desc, length(lastname) desc
                    """;
            /** expected
             * Clementine,Carey
             * Wallace,Christensen
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals("Clementine,Carey", result.getFirst());
            assertEquals("Wallace,Christensen", result.get(42));
        }

        @Test
        @DisplayName("Ne sachant encore pas exactement la manière dont le layout des cartes de membres sera organisé, il aimerait également savoir qui sont les 3 personnes qui ont, mis ensemble, la pair nom + prénom la plus longue.")
        void question10() {
            // TODO : Ecrire la requête SQL
            var query = """
                    select concat(firstname, ',', lastname)
                    from people
                    order by length(concat(firstname, '', lastname)) desc
                    limit 3
                    """;
            /** expected
             * Wallace,Christensen
             * Cheyenne,Pennington
             * Isabelle,Singleton
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals("Wallace,Christensen", result.getFirst());
            assertEquals("Cheyenne,Pennington", result.get(1));
            assertEquals("Isabelle,Singleton", result.getLast());
        }

        @Test
        @DisplayName("Il y a-t-il des doublons dans la table people")
        void question11() {
            // TODO : Ecrire la requête SQL
            var query = """
                    select concat(firstname, ',', lastname, ',', count(*))
                    from people
                    group by firstname, lastname
                    having count(*) > 1
                    """;
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
            assertEquals("Mara,Rollins,2", result.getFirst());
            assertEquals("Rahim,Nieves,2", result.get(1));
            assertEquals("Zahir,Warner,2", result.get(2));
            assertEquals("August,Singleton,2", result.get(3));
            assertEquals("Dennis,Kaufman,2", result.get(4));
            assertEquals("Forrest,Mckee,2", result.get(5));
            assertEquals("Sybill,Sparks,2", result.get(6));
            assertEquals("Mechelle,Craft,2", result.get(7));
            assertEquals("Kelly,Pennington,2", result.get(8));
            assertEquals("Darrel,Melendez,2", result.getLast());
        }


    }

    @Nested
    class Inviation {

        @Test
        @DisplayName("Pour l'ouverture, vous désirez lister tous les membres de plus de 18 ans, et de moins de 60 ans, qui ont une addresse email valide")
        void question12() {
            // TODO : Ecrire la requête SQL
            var query = """
                    select count(*)
                    from people
                    where email like '%@%'
                      and DATE_PART('YEAR', AGE(CURRENT_DATE, birthdate)) between 18 and 60
                    """;
            // expected : 404 records
            var typedQuery = entityManager.createNativeQuery(query);
            var result = (Long) typedQuery.getSingleResult();
            assertEquals(404, result);
        }

        @Test
        @DisplayName("Pour faciliter la lecture vous ajoutez une colonne age dans le résultat de votre requête")
        void question13() {
            // TODO : Ecrire la requête SQL
            var query = """
                    select *, DATE_PART('YEAR', AGE(CURRENT_DATE, birthdate)) as age
                    from people
                    where email like '%@%'
                      and DATE_PART('YEAR', AGE(CURRENT_DATE, birthdate)) between 18 and 60
                    """;
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
            var query = """
                    select concat(firstname, ' ', lastname, ' <', email, '>') as age
                    from people
                    where email like '%@%'
                      and DATE_PART('YEAR', AGE(CURRENT_DATE, birthdate)) between 18 and 60
                    """;
            /** expected
             * ex: Dawn Powell <nec@eueuismodac.com>
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            // TODO : Vérifier le résultat
            assertEquals("Dawn Powell <nec@eueuismodac.com>", result.getFirst());
        }

        @Test
        @DisplayName("Avec les informations contenues dans la table people (sans jointures), pourrait-on approximer le nombre de personnes habitant en Suisse ?")
        void question15() {
            // TODO : Ecrire la requête SQL
            var query = "";
            /** expected
             * 70
             */
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            // TODO : Vérifier le résultat
            assertEquals(410, result.size());
        }
    }

    @Nested
    class Jointure {

        @Test
        @DisplayName("En utilisant la table de jointure countries_people.sql, lister les personnes habitant en Suisse")
        void question16() {
            // TODO : Ecrire la requête SQL
            var query = """
                    select count(*)
                    from people
                    inner join countries_people cp on people.id = cp.idperson
                    inner join countries c on c.id = cp.idcountry
                    where c.name_fr = 'Suisse'
                    """;
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
            var query = """
                    select *
                    from people
                    inner join countries_people cp on people.id = cp.idperson
                    inner join countries c on c.id = cp.idcountry
                    where c.name_fr != 'Suisse'
                    """;
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals(43, result.size());
        }

        @Test
        @DisplayName("comment lister les personnes (nom et prénom) qui habitent dans les pays limitrophe de la Suisse ? (i.e France, Allemagne, Italie, Autriche, Lischenchtein)")
        void question18() {
            // TODO : Ecrire la requête SQL
            var query = """
                    select lastname, firstname
                    from people
                    inner join countries_people cp on people.id = cp.idperson
                    inner join countries c on c.id = cp.idcountry
                    where c.name_fr in ('France', 'Allemagne', 'Italie', 'Autriche', 'Lischenchtein')
                    """;
            // expected : 27 records
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals(27, result.size());
        }

        @Test
        @DisplayName("Vous souhaitez savoir combien il y a de personnes par pays, afin de savoir si votre table people a suffisament de personnes en suisse et combien de personnes sont étrangères.")
        void question19() {
            // TODO : Ecrire la requête SQL
            var query = """
                    select concat(iso2, ',', count(*))
                    from people
                             inner join countries_people cp on people.id = cp.idperson
                             inner join countries c on c.id = cp.idcountry
                    group by c.iso2
                    """;
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
            assertTrue(result.contains("VN,1"));
            assertTrue(result.contains("BA,1"));
            assertTrue(result.contains("DM,1"));
            assertTrue(result.contains("CH,371"));
            assertTrue(result.contains("SO,1"));
            assertTrue(result.contains("GB,3"));
            assertTrue(result.contains("IT,4"));
            assertTrue(result.contains("AT,6"));
            assertTrue(result.contains("LI,3"));
            assertTrue(result.contains("DE,5"));
            assertTrue(result.contains("SZ,1"));
            assertTrue(result.contains("TG,1"));
            assertTrue(result.contains("SY,1"));
            assertTrue(result.contains("BE,2"));
            assertTrue(result.contains("DZ,1"));
            assertTrue(result.contains("FR,12"));
        }

        @Test
        @DisplayName("Quels sont les pays qui ne possèdent pas de personnes")
        void question20() {
            // TODO : Ecrire la requête SQL
            var query = """
                    select c.name_fr
                    from countries c
                             left join countries_people cp on c.id = cp.idcountry
                    where cp.idcountry is null
                    """;
            // expected : 232 records
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals(232, result.size());
        }

        @Test
        @DisplayName("Il y a-t-il des personnes qui sont liées à plusieurs pays ? Si oui, lesquelles ?")
        void question21() {
            // TODO : Ecrire la requête SQL
            var query = """
                    select lastname, firstname
                    from people p
                             inner join public.countries_people cp on p.id = cp.idperson
                    group by lastname, firstname
                    having count(*) > 1
                    """;
            // expected : 12 records
            var typedQuery = entityManager.createNativeQuery(query);
            var result = typedQuery.getResultList();
            assertEquals(12, result.size());
        }

        @Test
        @DisplayName("Il y a-t-il des personnes liées à aucun pays ? Si oui, combien ?")
        void question22() {
            // TODO : Ecrire la requête SQL
            var query = """
                    select lastname, firstname
                    from people p
                             left join public.countries_people cp on p.id = cp.idperson
                    where cp.idcountry is null
                    """;
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
