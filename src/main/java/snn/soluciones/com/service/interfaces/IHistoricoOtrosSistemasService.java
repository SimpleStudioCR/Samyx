package snn.soluciones.com.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import snn.soluciones.com.models.entity.HistoricoOtrosSistemas;

public interface IHistoricoOtrosSistemasService {
  Page<HistoricoOtrosSistemas> findAllByIdEmisorAndId(Long paramLong, String paramString, Pageable paramPageable);
  
  HistoricoOtrosSistemas findByIdEmisorAndId(Long paramLong1, Long paramLong2);
}
