package snn.soluciones.com.service.impl;

import snn.soluciones.com.models.dao.IControlCajaMovimientosDao;
import snn.soluciones.com.models.entity.ControlCajaMovimientos;
import snn.soluciones.com.service.interfaces.IControlCajaMovimientosService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ControlCajaMovimientosServiceImpl implements IControlCajaMovimientosService {
  @Autowired
  private IControlCajaMovimientosDao _dao;
  
  public void save(ControlCajaMovimientos entity) {
    this._dao.save(entity);
  }
  
  public List<ControlCajaMovimientos> findAllByEmisorAndIdCaja(Long emisorId, Long cajaId) {
    return this._dao.findAllByEmisorAndIdCaja(emisorId, cajaId);
  }
  
  public Double totalEntradasSalidasEfectivo(Long emisorId, String tipoMovimiento, Long cajaId, String moneda) {
    return this._dao.totalEntradasSalidasEfectivo(emisorId, tipoMovimiento, cajaId, moneda);
  }
}
