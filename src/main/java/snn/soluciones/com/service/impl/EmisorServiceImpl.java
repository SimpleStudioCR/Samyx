package snn.soluciones.com.service.impl;

import snn.soluciones.com.models.dao.IEmisorDao;
import snn.soluciones.com.models.entity.Emisor;
import snn.soluciones.com.service.interfaces.IEmisorService;
import java.util.Date;
import java.util.List;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmisorServiceImpl implements IEmisorService {
  @Autowired
  private IEmisorDao _emisorDao;
  
  public Emisor findEmisorByIdentificacion(String identificacion, String tokenAccess) {
    return this._emisorDao.findEmisorByIdentificacion(identificacion, tokenAccess);
  }
  
  public List<Emisor> findAll() {
    return (List<Emisor>)this._emisorDao.findAll();
  }
  
  public Emisor findById(Long id) {
    return this._emisorDao.findById(id).orElse(null);
  }
  
  @Transactional
  public void save(Emisor entity) {
    this._emisorDao.save(entity);
  }
  
  @Transactional
  public void updateFechaCriptografica(Date fecha_caducidad_cert, Date fecha_emision_cert, Long emisorId) {
    this._emisorDao.updateFechaCriptografica(fecha_caducidad_cert, fecha_emision_cert, emisorId);
  }
}
