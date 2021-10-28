package be.vdab.luigi.repositories;

import be.vdab.luigi.domain.Pizza;
import be.vdab.luigi.exceptions.PizzaNietGevondenException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class JdbcPizzaRepository implements PizzaRepository {
    //JdbcTemplate bean wordt aangemaakt bij het starten van de website dankzij de JDBC dependency
    //Spring injecteert DataSource bean in JdbcTemplate bean
    //template = query, update
    private final JdbcTemplate template;

    //insert = insert
    private final SimpleJdbcInsert insert;


    //RowMapper interface heeft maar een method: mapRow
    private final RowMapper<Pizza> pizzaMapper =
            //parameters van de method:
            //1ste parameter = ResultSet
            //2de parameter = volgnummer van huidige rij in ResultSet
            (result, rowNum) ->
                    //return een Pizza op basis van de huidige rij
                    new Pizza(result.getLong("id"), result.getString("naam"),
                            result.getBigDecimal("prijs"), result.getBoolean("pikant"));

    private final RowMapper<BigDecimal> prijsMapper =
            (result, rowNum) -> result.getBigDecimal("prijs");

    //Injecteer de JdbcTemplate bean in je Repository bean
    //Zo kunnen we een database connectie vragen aan de DataSource (verzameling connections)
    public JdbcPizzaRepository(JdbcTemplate template) {
        this.template = template;
        //SimpleJdbcInsert zal records toevoegen met een connection uit de DataSource injectie van je JdbcTemplate
        insert = new SimpleJdbcInsert(template)
                .withTableName("pizzas")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public long create(Pizza pizza) {
        //de method maakt zelf een SQL insert statement en voert dit uit
        //en geeft de automatisch gegenereerde primary key waarde als een Number, neem daarvan de long
        return insert.executeAndReturnKey(
                        //de key in K,V paar is de naam van je kolom
                        Map.of("naam", pizza.getNaam(),
                                "prijs", pizza.getPrijs(),
                                "pikant", pizza.isPikant()))
                .longValue();
    }

    @Override
    public void update(Pizza pizza) {
        var sql = """
                update pizzas
                set naam = ?, prijs = ?, pikant = ?
                where id = ?
                """;
        //update method returnt het aantal aangepaste records, als er geen aangepast zijn wil dat zeggen PizzaNietGevonden
        if (template.update(sql, pizza.getNaam(), pizza.getPrijs(), pizza.isPikant(),
                pizza.getId()) == 0) {
            throw new PizzaNietGevondenException();
        }

    }

    @Override
    public void delete(long id) {
        var sql = """
                delete from pizzas
                where id = ?
                """;
        template.update(sql, id);
    }

    @Override
    public List<Pizza> findAll() {
        var sql = """
                select id, naam, prijs, pikant
                from pizzas
                order by id
                """;
        return template.query(sql, pizzaMapper);
    }

    @Override
    public Optional<Pizza> findById(long id) {
        try {
            var sql = """
                    select id, naam, prijs, pikant
                    from pizzas
                    where id = ?
                    """;
            return Optional.of(template.queryForObject(sql, pizzaMapper, id));
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Pizza> findByPrijsBetween(BigDecimal van, BigDecimal tot) {
        var sql = """
                select id, naam, prijs, pikant
                from pizzas
                where prijs between ? and ?
                order by prijs
                """;
        return template.query(sql, pizzaMapper, van, tot);
    }

    @Override
    public long findAantal() {
        var sql = """
                select count(*)
                from pizzas
                """;
        return template.queryForObject(sql, Long.class);
    }

    @Override
    public List<BigDecimal> findUniekePrijzen() {
        var sql = """
                select distinct prijs
                from pizzas
                order by prijs
                """;
        return template.query(sql, prijsMapper);
    }

    @Override
    public List<Pizza> findByPrijs(BigDecimal prijs) {
        var sql = """
                select id, naam, prijs, pikant
                from pizzas
                where prijs = ?
                order by naam
                """;
        return template.query(sql, pizzaMapper, prijs);
    }

    /*WERKT NIET*/
    @Override
    public List<Pizza> findByIds(Set<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        var sql = """
                select id, naam, prijs, pikant
                from pizzas
                where id in (
                """ + "?,".repeat(ids.size() - 1) + "?) order by id";
        return template.query(sql, pizzaMapper, ids.toArray());
    }
}
