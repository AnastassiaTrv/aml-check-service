package ana.tret.amlcheckservice.repository.sanctionedsubject;

import ana.tret.amlcheckservice.dto.sanctionedsubject.SubjectMatchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SanctionedSubjectJdbcRepo {
    private final NamedParameterJdbcTemplate jdbc;

    public List<SubjectMatchDto> preselectOrderedBySimilarity(String normalized, int limit) {
        String sql = """
            SELECT id, full_name, normalized_name, similarity(normalized_name, :normalized) AS rate
            FROM sanctioned_subject
            ORDER BY normalized_name <-> :normalized
            LIMIT :limit;
            """;
        Map<String, Object> params = Map.of(
                "normalized", normalized,
                "limit", limit
        );
        return jdbc.query(sql, params, ROW_MAPPER);
    }

    private static final RowMapper<SubjectMatchDto> ROW_MAPPER = (rs, i) -> new SubjectMatchDto(
            rs.getLong("id"),
            rs.getString("full_name"),
            rs.getString("normalized_name"),
            rs.getDouble("rate")
    );
}
