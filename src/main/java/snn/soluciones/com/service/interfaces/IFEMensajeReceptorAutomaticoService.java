package snn.soluciones.com.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import snn.soluciones.com.models.entity.FEMensajeReceptorAutomatico;

public interface IFEMensajeReceptorAutomaticoService {
  Page<FEMensajeReceptorAutomatico> findAllByReceptor(String paramString1, String paramString2, String paramString3, String paramString4, Pageable paramPageable);
  
  FEMensajeReceptorAutomatico findById(Long paramLong);
  
  void updateEstadoMrInbox(String paramString, Long paramLong);
}
