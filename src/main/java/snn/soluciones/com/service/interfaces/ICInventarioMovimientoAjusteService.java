package snn.soluciones.com.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import snn.soluciones.com.models.entity.CInventarioAjuste;

public interface ICInventarioMovimientoAjusteService {
  void save(CInventarioAjuste paramCInventarioAjuste);
  
  Page<CInventarioAjuste> findAllByEmisorId(Long paramLong, String paramString, Pageable paramPageable);
  
  CInventarioAjuste findByEmisorIdAndIdFactura(Long paramLong1, Long paramLong2);
  
  CInventarioAjuste numeroAjuste(Long paramLong);
}
