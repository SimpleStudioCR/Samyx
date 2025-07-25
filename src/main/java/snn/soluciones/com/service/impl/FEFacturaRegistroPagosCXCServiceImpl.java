package snn.soluciones.com.service.impl;

import snn.soluciones.com.models.dao.IFEFacturaRegistroPagosCXCDao;
import snn.soluciones.com.models.entity.FEFacturaRegistroPagosCXC;
import snn.soluciones.com.service.interfaces.IFEFacturaRegistroPagosCXCService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FEFacturaRegistroPagosCXCServiceImpl implements IFEFacturaRegistroPagosCXCService {
  @Autowired
  private IFEFacturaRegistroPagosCXCDao _dao;
  
  public List<FEFacturaRegistroPagosCXC> findAllPagosCXC(Long emisorId, Long clienteId, Long facturaId) {
    return this._dao.findAllPagosCXC(emisorId, clienteId, facturaId);
  }
  
  public void save(FEFacturaRegistroPagosCXC entity) {
    this._dao.save(entity);
  }
  
  public FEFacturaRegistroPagosCXC findMaxFacturaByEmisor(Long emisorId) {
    return this._dao.findMaxFacturaByEmisor(emisorId);
  }
  
  public List<FEFacturaRegistroPagosCXC> reporteFlujoCajaCXC(Long emisorId, Date f1, Date f2, List<Long> mp) {
    return this._dao.reporteFlujoCajaCXC(emisorId, f1, f2, mp);
  }
}
