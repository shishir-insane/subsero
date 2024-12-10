package com.sr.subsero.analytic;

import com.sr.subsero.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AnalyticService {

    private final AnalyticRepository analyticRepository;

    public AnalyticService(final AnalyticRepository analyticRepository) {
        this.analyticRepository = analyticRepository;
    }

    public List<AnalyticDTO> findAll() {
        final List<Analytic> analytics = analyticRepository.findAll(Sort.by("id"));
        return analytics.stream()
                .map(analytic -> mapToDTO(analytic, new AnalyticDTO()))
                .toList();
    }

    public AnalyticDTO get(final Long id) {
        return analyticRepository.findById(id)
                .map(analytic -> mapToDTO(analytic, new AnalyticDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AnalyticDTO analyticDTO) {
        final Analytic analytic = new Analytic();
        mapToEntity(analyticDTO, analytic);
        return analyticRepository.save(analytic).getId();
    }

    public void update(final Long id, final AnalyticDTO analyticDTO) {
        final Analytic analytic = analyticRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(analyticDTO, analytic);
        analyticRepository.save(analytic);
    }

    public void delete(final Long id) {
        analyticRepository.deleteById(id);
    }

    private AnalyticDTO mapToDTO(final Analytic analytic, final AnalyticDTO analyticDTO) {
        analyticDTO.setId(analytic.getId());
        analyticDTO.setTotalMonthlyCost(analytic.getTotalMonthlyCost());
        analyticDTO.setTotalAnnualCost(analytic.getTotalAnnualCost());
        analyticDTO.setMostExpensiveSubscription(analytic.getMostExpensiveSubscription());
        analyticDTO.setLeastUsedSubscription(analytic.getLeastUsedSubscription());
        analyticDTO.setUpdatedAt(analytic.getUpdatedAt());
        return analyticDTO;
    }

    private Analytic mapToEntity(final AnalyticDTO analyticDTO, final Analytic analytic) {
        analytic.setTotalMonthlyCost(analyticDTO.getTotalMonthlyCost());
        analytic.setTotalAnnualCost(analyticDTO.getTotalAnnualCost());
        analytic.setMostExpensiveSubscription(analyticDTO.getMostExpensiveSubscription());
        analytic.setLeastUsedSubscription(analyticDTO.getLeastUsedSubscription());
        analytic.setUpdatedAt(analyticDTO.getUpdatedAt());
        return analytic;
    }

}
