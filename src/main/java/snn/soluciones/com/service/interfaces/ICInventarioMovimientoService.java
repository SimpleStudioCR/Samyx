package snn.soluciones.com.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import snn.soluciones.com.models.entity.CInventarioMovimiento;

public interface ICInventarioMovimientoService {
  void save(CInventarioMovimiento paramCInventarioMovimiento);
  
  Page<CInventarioMovimiento> findAllByEmisorId(Long paramLong, String paramString, Pageable paramPageable);
  
  CInventarioMovimiento findByEmisorIdAndIdFactura(Long paramLong1, Long paramLong2);
  
  CInventarioMovimiento secuenciaFacturaCompra(Long paramLong);
}
