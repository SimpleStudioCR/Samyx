package snn.soluciones.com.service.impl;

import snn.soluciones.com.models.dao.IEmisorActividadesDao;
import snn.soluciones.com.models.entity.EmisorActividades;
import snn.soluciones.com.service.interfaces.IEmisorActividadesService;
import java.util.List;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmisorActividadesServiceImpl implements IEmisorActividadesService {
  @Autowired
  private IEmisorActividadesDao _dao;
  
  public List<EmisorActividades> findAllByEmisorId(Long emisorId) {
    return this._dao.findAllByEmisorId(emisorId);
  }
  
  @Transactional
  public void deleteByIdAndEmisorId(Long id, Long emisorId) {
    this._dao.deleteByIdAndEmisorId(id, emisorId);
  }
  
  public void save(EmisorActividades entity) {
    this._dao.save(entity);
  }
}
