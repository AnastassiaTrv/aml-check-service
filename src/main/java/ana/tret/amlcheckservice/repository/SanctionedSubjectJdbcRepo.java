package ana.tret.amlcheckservice.repository;

import ana.tret.amlcheckservice.domain.SanctionedSubject;
import ana.tret.amlcheckservice.domain.SubjectMatch;
import ana.tret.amlcheckservice.exception.DuplicateRecordException;
import ana.tret.amlcheckservice.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

import static java.lang.Boolean.TRUE;

@Repository
@RequiredArgsConstructor
public class SanctionedSubjectJdbcRepo {
    private final NamedParameterJdbcTemplate jdbc;

    public List<SubjectMatch> preselectOrderedBySimilarity(String normalized, int limit) {
        String sql = """
            SELECT id, full_name, normalized_name, similarity(normalized_name, :normalized) AS rate
            FROM sanctioned_subject
            ORDER BY normalized_name <-> :normalized
            LIMIT :limit;
            """;

        var params = new MapSqlParameterSource()
                .addValue("normalized", normalized)
                .addValue("limit", limit);

        return jdbc.query(sql, params, SUBJECT_MATCH_MAPPER);
    }

    public SanctionedSubject insert(String fullName, String normalized) {
        String sql = """
                INSERT INTO sanctioned_subject(full_name, normalized_name, created_at, updated_at)
                VALUES (:fullName, :normalizedName, now(), now())
                RETURNING id, full_name, normalized_name, created_at, updated_at, version
            """;

        var params = new MapSqlParameterSource()
                .addValue("fullName", fullName)
                .addValue("normalizedName", normalized);

        try {
            return jdbc.queryForObject(sql, params, SUBJECT_MAPPER);
        } catch (DuplicateKeyException e) {
            throw new DuplicateRecordException("Duplicate normalized_name not allowed: %s".formatted(normalized), e);
        }
    }

    public SanctionedSubject update(Long id, String fullName, String normalized, Long expectedVersion) {
        String sql = """
        UPDATE sanctioned_subject
        SET full_name       = :fullName,
            normalized_name = :normalized,
            updated_at      = now(),
            version         = version + 1
        WHERE id = :id
          AND version = :expectedVersion
        RETURNING id, full_name, normalized_name, created_at, updated_at, version
        """;

        var params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("fullName", fullName)
                .addValue("normalized", normalized)
                .addValue("expectedVersion", expectedVersion);
        try {
            return jdbc.queryForObject(sql, params, SUBJECT_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            if (!existsById(id)) {
                throw new RecordNotFoundException("Record not found by id=%d".formatted(id), e);
            }
            throw new OptimisticLockingFailureException("Update failed for id=%d, expectedVersion=%d".formatted(id, expectedVersion), e);
        } catch (DuplicateKeyException e) {
            throw new DuplicateRecordException("Duplicate normalized_name not allowed: %s".formatted(normalized), e);
        }
    }

    public boolean existsById(Long id) {
        String sql = "SELECT EXISTS(SELECT 1 from sanctioned_subject WHERE id=:id)";
        return TRUE.equals(jdbc.queryForObject(sql, new MapSqlParameterSource("id", id), Boolean.class));
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM sanctioned_subject WHERE id = :id";
        jdbc.update(sql, new MapSqlParameterSource("id", id));
    }

    private static final RowMapper<SubjectMatch> SUBJECT_MATCH_MAPPER = (rs, i) -> new SubjectMatch(
            rs.getLong("id"),
            rs.getString("full_name"),
            rs.getString("normalized_name"),
            rs.getDouble("rate")
    );

    private static final RowMapper<SanctionedSubject> SUBJECT_MAPPER = (rs, i) -> new SanctionedSubject(
            rs.getLong("id"),
            rs.getString("full_name"),
            rs.getString("normalized_name"),
            rs.getObject("created_at", OffsetDateTime.class),
            rs.getObject("updated_at", OffsetDateTime.class),
            rs.getLong("version")
    );
}
