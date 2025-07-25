package snn.soluciones.com.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import snn.soluciones.com.models.entity.CCodigoReferencia;
import snn.soluciones.com.models.entity.CCodigosTarifasIva;
import snn.soluciones.com.models.entity.CCondicionDeVenta;
import snn.soluciones.com.models.entity.CImpuesto;
import snn.soluciones.com.models.entity.CMedioDePago;
import snn.soluciones.com.models.entity.COtrosCargosTipoDocumento;
import snn.soluciones.com.models.entity.CProvincia;
import snn.soluciones.com.models.entity.CSucursal;
import snn.soluciones.com.models.entity.CTerminal;
import snn.soluciones.com.models.entity.CTipoDeCambio;
import snn.soluciones.com.models.entity.CTipoDeDocumentoReferencia;
import snn.soluciones.com.models.entity.CTipoDeIdentificacion;
import snn.soluciones.com.models.entity.CTipoDocumentoExoneracionOAutorizacion;
import snn.soluciones.com.models.entity.Cliente;
import snn.soluciones.com.models.entity.Emisor;
import snn.soluciones.com.models.entity.EmisorActividades;
import snn.soluciones.com.models.entity.FEBitacora;
import snn.soluciones.com.models.entity.FEExoneracionImpuestoItemFactura;
import snn.soluciones.com.models.entity.FEFactura;
import snn.soluciones.com.models.entity.FEFacturaItem;
import snn.soluciones.com.models.entity.FEFacturaOtrosCargos;
import snn.soluciones.com.models.entity.FEFacturaReferencia;
import snn.soluciones.com.models.entity.FEFacturasCXC;
import snn.soluciones.com.models.entity.FEImpuestosItemFactura;
import snn.soluciones.com.models.entity.Moneda;
import snn.soluciones.com.models.entity.Producto;
import snn.soluciones.com.models.entity.UnidadDeMedida;
import snn.soluciones.com.models.entity.Usuario;
import snn.soluciones.com.service.impl.COtrosCargosTipoDocumentoImpl;
import snn.soluciones.com.service.impl.FuncionesService;
import snn.soluciones.com.service.interfaces.ICCodigoReferenciaService;
import snn.soluciones.com.service.interfaces.ICCodigosTarifasIvaService;
import snn.soluciones.com.service.interfaces.ICCondicionDeVentaService;
import snn.soluciones.com.service.interfaces.ICImpuestoService;
import snn.soluciones.com.service.interfaces.ICMedioDePagoService;
import snn.soluciones.com.service.interfaces.ICProductoService;
import snn.soluciones.com.service.interfaces.ICProvinciaService;
import snn.soluciones.com.service.interfaces.ICSucursalService;
import snn.soluciones.com.service.interfaces.ICTerminalService;
import snn.soluciones.com.service.interfaces.ICTipoDeCambioService;
import snn.soluciones.com.service.interfaces.ICTipoDeDocumentoReferenciaService;
import snn.soluciones.com.service.interfaces.ICTipoDeIdentificacionService;
import snn.soluciones.com.service.interfaces.ICTipoDocumentoExoneracionOAutorizacionService;
import snn.soluciones.com.service.interfaces.IClienteService;
import snn.soluciones.com.service.interfaces.IEmisorActividadesService;
import snn.soluciones.com.service.interfaces.IEmisorService;
import snn.soluciones.com.service.interfaces.IFEFacturaService;
import snn.soluciones.com.service.interfaces.IFEFacturasCXCServices;
import snn.soluciones.com.service.interfaces.IMonedaService;
import snn.soluciones.com.service.interfaces.IUnidadDeMedidaService;
import snn.soluciones.com.service.interfaces.IUsuarioService;
import snn.soluciones.com.util.PageRender;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping({"/prefacturas"})
public class PrefacturasController {
  @Autowired
  private ICProductoService _productoService;
  
  @Autowired
  private IClienteService _clienteService;
  
  @Autowired
  private IMonedaService _monedaService;
  
  @Autowired
  private FuncionesService _funcionesService;
  
  @Autowired
  private ICCondicionDeVentaService _condicionDeVentaService;
  
  @Autowired
  private IUnidadDeMedidaService _unidadDeMedidaService;
  
  @Autowired
  private ICMedioDePagoService _medioDePagoService;
  
  @Autowired
  private ICImpuestoService _impuestoService;
  
  @Autowired
  private ICCodigoReferenciaService _codigoReferenciaService;
  
  @Autowired
  private IEmisorService _emisorService;
  
  @Autowired
  private IUsuarioService _usuarioService;
  
  @Autowired
  private IFEFacturaService _facturaService;
  
  @Autowired
  private ICSucursalService _sucursalService;
  
  @Autowired
  private ICTerminalService _terminalService;
  
  @Autowired
  private ICTipoDeCambioService _tipoDeCambioService;
  
  @Autowired
  private ICTipoDeIdentificacionService _tipoDeIdentificacionService;
  
  @Autowired
  private COtrosCargosTipoDocumentoImpl _otorsCargosService;
  
  @Autowired
  private ICTipoDocumentoExoneracionOAutorizacionService _exoneraTipoDocService;
  
  @Autowired
  private ICProvinciaService _provinciaService;
  
  @Autowired
  private IFEFacturasCXCServices _cxcService;
  
  @Autowired
  private ICCodigosTarifasIvaService _tarifasIvaService;
  
  @Autowired
  public JavaMailSender emailSender;
  
  @Autowired
  public ICTipoDeDocumentoReferenciaService tipoDocReferenciaService;
  
  @Autowired
  public IEmisorActividadesService _actividadesService;
  
  @Autowired
  private ICCodigosTarifasIvaService _codigosTarifasIvaService;
  
  @Autowired
  public javax.sql.DataSource dataSource;
  
  @Value("${path.upload.files.api}")
  private String pathUploadFilesApi;
  
  @Value("${url.qr}")
  private String urlQr;
  
  @Value("${correo.de.distribucion}")
  private String correoDistribucion;
  
  private final Logger log = LoggerFactory.getLogger(getClass());
  
  @Value("${api.jmata.recepcion}")
  private String apiJmataRecepcion;
  
  private String ACTION_FORM = "/prefacturas/edit/";
  
  private String TIPO_FORM = "";
  
  private String MENSAJE_FORM1 = "Prefactura guardada con éxito";
  
  private String MENSAJE_FORM2 = "guardada con redireccionando a la prefactura guardada!";
  
  private String MENSAJE_FORM3 = "se pueden guardar si el tipo de documento corresponde a una prefactura!";
  
  @GetMapping({"/"})
  public String home(Model model, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "q", defaultValue = "") String q, HttpSession session) {
    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
      if (session.getAttribute("SESSION_SUCURSAL_ID") != null && session
        .getAttribute("SESSION_TERMINAL_ID") != null) {
        Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
        PageRequest pageRequest = PageRequest.of(page, 15);
        Page<FEFactura> ListaDocumentos = null;
        ListaDocumentos = this._facturaService.findProformasByEmisorId(emisorId, q.toUpperCase(), (Pageable)pageRequest);
        PageRender<FEFactura> pageRender = new PageRender("/prefacturas/", ListaDocumentos);
        model.addAttribute("ListaDocumentos", ListaDocumentos);
        model.addAttribute("page", pageRender);
        return "facturacion/prefacturas/index";
      } 
      return "redirect:/proformas/seleccionar-terminal?urlRetorno=/prefacturas/";
    } 
    return "redirect:/";
  }
  
  @GetMapping({"/nueva"})
  public String form(Model model, HttpSession session, Authentication auth) {
    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
      Long emisorId = Long.valueOf(session.getAttribute("SESSION_EMPRESA_ID").toString());
      if (session.getAttribute("SESSION_SUCURSAL_ID") != null && session.getAttribute("SESSION_TERMINAL_ID") != null) {
        List<Moneda> listaMonedas = this._monedaService.findAll();
        List<UnidadDeMedida> listaUnidadDeMedida = this._unidadDeMedidaService.findAll();
        List<CCondicionDeVenta> listaCondicionDeVenta = this._condicionDeVentaService.findAll();
        List<CMedioDePago> listaMedioDePago = this._medioDePagoService.findAll();
        List<CImpuesto> listaImpuestos = this._impuestoService.findAllImpuestos();
        List<CCodigoReferencia> listaCodigoReferencia = this._codigoReferenciaService.findAll();
        List<CTipoDeCambio> listaTiposDeCambio = this._tipoDeCambioService.findAllDivisas();
        List<COtrosCargosTipoDocumento> listaTipoDocOtrosCargos = this._otorsCargosService.findAll();
        List<CTipoDeDocumentoReferencia> listaTipoDocReferencia = this.tipoDocReferenciaService.findAll();
        List<EmisorActividades> listaActividades = this._actividadesService.findAllByEmisorId(emisorId);
        List<CTipoDocumentoExoneracionOAutorizacion> listaTipoDocsExonera = this._exoneraTipoDocService.findAll();
        List<CCodigosTarifasIva> listaCodigosTarifasIva = this._codigosTarifasIvaService.findAll();
        Cliente cliente = new Cliente();
        List<CTipoDeIdentificacion> listaTipoDeIdentificacion = this._tipoDeIdentificacionService.findAll();
        List<CProvincia> listaProvincias = this._provinciaService.findAll();
        FEFactura factura = new FEFactura();
        model.addAttribute("listaActividades", listaActividades);
        model.addAttribute("listaTipoDeIdentificacion", listaTipoDeIdentificacion);
        model.addAttribute("listaProvincias", listaProvincias);
        model.addAttribute("cliente", cliente);
        model.addAttribute("listaTipoDocsExonera", listaTipoDocsExonera);
        model.addAttribute("listaMonedas", listaMonedas);
        model.addAttribute("listaUnidadDeMedida", listaUnidadDeMedida);
        model.addAttribute("listaCondicionDeVenta", listaCondicionDeVenta);
        model.addAttribute("listaMedioDePago", listaMedioDePago);
        model.addAttribute("listaImpuestos", listaImpuestos);
        model.addAttribute("listaCodigoReferencia", listaCodigoReferencia);
        model.addAttribute("listaCodigosTarifasIva", listaCodigosTarifasIva);
        model.addAttribute("listaTiposDeCambio", listaTiposDeCambio);
        model.addAttribute("listaTipoDocOtrosCargos", listaTipoDocOtrosCargos);
        model.addAttribute("listaTipoDocReferencia", listaTipoDocReferencia);
        model.addAttribute("TIPO_DOC_USUARIO", session.getAttribute("SESSION_PARAM_TIPO_DOC").toString());
        if (session.getAttribute("SESSION_PARAM_TIPO_BUSQUEDA_PRODUCTO") != null && session.getAttribute("SESSION_PARAM_TIPO_BUSQUEDA_PRODUCTO").toString().equalsIgnoreCase("S")) {
          model.addAttribute("TIPO_BUSQUEDA_PRODUCTO", "S");
        } else {
          model.addAttribute("TIPO_BUSQUEDA_PRODUCTO", "M");
        } 
        model.addAttribute("MODIFICA_PRECIO_FACTURACION", session.getAttribute("SESSION_PARAM_MODIFICA_PRECIO_FACTURACION").toString());
        model.addAttribute("APLICA_DESCUENTO", session.getAttribute("SESSION_PARAM_APLICA_DESCUENTO").toString());
        model.addAttribute("MONTO_MAXIMO_DESCUENTO", session.getAttribute("SESSION_PARAM_MONTO_MAX_DESCUENTO").toString());
        if (session.getAttribute("SESSION_PARAM_MONEDA_USUARIO") != null) {
          model.addAttribute("MONEDA_USUARIO", session.getAttribute("SESSION_PARAM_MONEDA_USUARIO").toString());
        } else {
          model.addAttribute("MONEDA_USUARIO", Integer.valueOf(1));
        } 
        model.addAttribute("INCLUIR_OMITIR_RECEPTOR", session.getAttribute("SESSION_PARAM_INCLUIR_OMITIR_RECEPTOR").toString());
        model.addAttribute("SUCURSAL_ID", session.getAttribute("SESSION_SUCURSAL_ID"));
        model.addAttribute("TERMINAL_ID", session.getAttribute("SESSION_TERMINAL_ID"));
        model.addAttribute("SESSION_SUCURSAL", session.getAttribute("SESSION_SUCURSAL"));
        model.addAttribute("SESSION_TERMINAL", session.getAttribute("SESSION_TERMINAL"));
        model.addAttribute("SUCURSAL_ID", session.getAttribute("SESSION_SUCURSAL_ID"));
        model.addAttribute("TERMINAL_ID", session.getAttribute("SESSION_TERMINAL_ID"));
        model.addAttribute("SESSION_SUCURSAL", session.getAttribute("SESSION_SUCURSAL"));
        model.addAttribute("SESSION_TERMINAL", session.getAttribute("SESSION_TERMINAL"));
        model.addAttribute("factura", factura);
        model.addAttribute("ACTION_FORM", this.ACTION_FORM);
        model.addAttribute("TIPO_FORM", this.TIPO_FORM);
        model.addAttribute("MENSAJE_FORM1", this.MENSAJE_FORM1);
        model.addAttribute("MENSAJE_FORM2", this.MENSAJE_FORM2);
        model.addAttribute("MENSAJE_FORM3", this.MENSAJE_FORM3);
        return "facturacion/prefacturas/facturador";
      } 
      return "redirect:/proformas/seleccionar-terminal?urlRetorno=/prefacturas/";
    } 
    return "redirect:/";
  }
  
  @GetMapping({"/edit/{id}"})
  public String formEdit(Model model, @PathVariable("id") Long id, HttpSession session, Authentication auth) {
    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
      Long emisorId = Long.valueOf(session.getAttribute("SESSION_EMPRESA_ID").toString());
      FEFactura _factura = this._facturaService.findByEmisorById(emisorId, id);
      List<Moneda> listaMonedas = this._monedaService.findAll();
      List<UnidadDeMedida> listaUnidadDeMedida = this._unidadDeMedidaService.findAll();
      List<CCondicionDeVenta> listaCondicionDeVenta = this._condicionDeVentaService.findAll();
      List<CMedioDePago> listaMedioDePago = this._medioDePagoService.findAll();
      List<CImpuesto> listaImpuestos = this._impuestoService.findAllImpuestos();
      List<CCodigoReferencia> listaCodigoReferencia = this._codigoReferenciaService.findAll();
      List<CTipoDeCambio> listaTiposDeCambio = this._tipoDeCambioService.findAllDivisas();
      List<COtrosCargosTipoDocumento> listaTipoDocOtrosCargos = this._otorsCargosService.findAll();
      List<CTipoDeDocumentoReferencia> listaTipoDocReferencia = this.tipoDocReferenciaService.findAll();
      List<EmisorActividades> listaActividades = this._actividadesService.findAllByEmisorId(emisorId);
      List<CTipoDocumentoExoneracionOAutorizacion> listaTipoDocsExonera = this._exoneraTipoDocService.findAll();
      List<CCodigosTarifasIva> listaCodigosTarifasIva = this._codigosTarifasIvaService.findAll();
      Cliente cliente = new Cliente();
      List<CTipoDeIdentificacion> listaTipoDeIdentificacion = this._tipoDeIdentificacionService.findAll();
      List<CProvincia> listaProvincias = this._provinciaService.findAll();
      model.addAttribute("listaActividades", listaActividades);
      model.addAttribute("listaTipoDeIdentificacion", listaTipoDeIdentificacion);
      model.addAttribute("listaProvincias", listaProvincias);
      model.addAttribute("cliente", cliente);
      model.addAttribute("listaTipoDocsExonera", listaTipoDocsExonera);
      model.addAttribute("listaMonedas", listaMonedas);
      model.addAttribute("listaUnidadDeMedida", listaUnidadDeMedida);
      model.addAttribute("listaCondicionDeVenta", listaCondicionDeVenta);
      model.addAttribute("listaMedioDePago", listaMedioDePago);
      model.addAttribute("listaImpuestos", listaImpuestos);
      model.addAttribute("listaCodigosTarifasIva", listaCodigosTarifasIva);
      model.addAttribute("listaCodigoReferencia", listaCodigoReferencia);
      model.addAttribute("listaTiposDeCambio", listaTiposDeCambio);
      model.addAttribute("listaTipoDocOtrosCargos", listaTipoDocOtrosCargos);
      model.addAttribute("listaTipoDocReferencia", listaTipoDocReferencia);
      model.addAttribute("TIPO_DOC_USUARIO", session.getAttribute("SESSION_PARAM_TIPO_DOC").toString());
      if (session.getAttribute("SESSION_PARAM_TIPO_BUSQUEDA_PRODUCTO") != null && session.getAttribute("SESSION_PARAM_TIPO_BUSQUEDA_PRODUCTO").toString().equalsIgnoreCase("S")) {
        model.addAttribute("TIPO_BUSQUEDA_PRODUCTO", "S");
      } else {
        model.addAttribute("TIPO_BUSQUEDA_PRODUCTO", "M");
      } 
      model.addAttribute("MODIFICA_PRECIO_FACTURACION", session.getAttribute("SESSION_PARAM_MODIFICA_PRECIO_FACTURACION").toString());
      model.addAttribute("APLICA_DESCUENTO", session.getAttribute("SESSION_PARAM_APLICA_DESCUENTO").toString());
      model.addAttribute("MONTO_MAXIMO_DESCUENTO", session.getAttribute("SESSION_PARAM_MONTO_MAX_DESCUENTO").toString());
      if (session.getAttribute("SESSION_PARAM_MONEDA_USUARIO") != null) {
        model.addAttribute("MONEDA_USUARIO", session.getAttribute("SESSION_PARAM_MONEDA_USUARIO").toString());
      } else {
        model.addAttribute("MONEDA_USUARIO", Integer.valueOf(1));
      } 
      model.addAttribute("INCLUIR_OMITIR_RECEPTOR", session.getAttribute("SESSION_PARAM_INCLUIR_OMITIR_RECEPTOR").toString());
      model.addAttribute("SUCURSAL_ID", session.getAttribute("SESSION_SUCURSAL_ID"));
      model.addAttribute("TERMINAL_ID", session.getAttribute("SESSION_TERMINAL_ID"));
      model.addAttribute("SESSION_SUCURSAL", session.getAttribute("SESSION_SUCURSAL"));
      model.addAttribute("SESSION_TERMINAL", session.getAttribute("SESSION_TERMINAL"));
      model.addAttribute("SUCURSAL_ID", session.getAttribute("SESSION_SUCURSAL_ID"));
      model.addAttribute("TERMINAL_ID", session.getAttribute("SESSION_TERMINAL_ID"));
      model.addAttribute("SESSION_SUCURSAL", session.getAttribute("SESSION_SUCURSAL"));
      model.addAttribute("SESSION_TERMINAL", session.getAttribute("SESSION_TERMINAL"));
      if (!_factura.getEstado().equalsIgnoreCase("P"))
        return "redirect:/prefacturas/"; 
      model.addAttribute("ACTION_FORM", this.ACTION_FORM);
      model.addAttribute("TIPO_FORM", this.TIPO_FORM);
      model.addAttribute("MENSAJE_FORM1", this.MENSAJE_FORM1);
      model.addAttribute("MENSAJE_FORM2", this.MENSAJE_FORM2);
      model.addAttribute("MENSAJE_FORM3", this.MENSAJE_FORM3);
      model.addAttribute("factura", _factura);
      return "facturacion/prefacturas/facturador";
    } 
    return "redirect:/";
  }
  
  @PostMapping(value = {"/emitir-factura-nodisponible"}, produces = {"application/json"})
  public ResponseEntity<?> emiteFactura(FEFactura factura, @RequestParam(name = "idLinea[]", required = false) Long[] idLinea, @RequestParam(name = "item_id[]", required = false) Long[] itemId, @RequestParam(name = "cantidad[]", required = false) Double[] cantidad, @RequestParam(name = "partidaArancelaria[]", required = false) String[] partidaArancelaria, @RequestParam(name = "detalleProducto[]", required = false) String[] detalleProducto, @RequestParam(name = "codigoProducto1[]", required = false) String[] codigoProducto1, @RequestParam(name = "codigoProducto2[]", required = false) String[] codigoProducto2, @RequestParam(name = "unidadDeMedida[]", required = false) String[] unidadDeMedida, @RequestParam(name = "nombreProducto[]", required = false) String[] nombreProducto, @RequestParam(name = "precioUnitario[]", required = false) Double[] precioUnitario, @RequestParam(name = "montoTotal[]", required = false) Double[] _montoTotal, @RequestParam(name = "descuentoTotal[]", required = false) Double[] descuentoTotal, @RequestParam(name = "subtotal[]", required = false) Double[] subTotal, @RequestParam(name = "impuestoTotal[]", required = false) Double[] impuestoTotal, @RequestParam(name = "totalLinea[]", required = false) Double[] totalLinea, @RequestParam(name = "impuestoTarifaIva[]", required = false) String[] impuestoTarifaIva, @RequestParam(name = "impuestoNeto[]", required = false) String[] impuestoNeto, @RequestParam(name = "correoCliente", required = false) String correoCliente, HttpServletRequest request, Authentication authentication, HttpSession session) throws ClientProtocolException, IOException, ParseException {
    Map<String, Object> response = new HashMap<>();
    if (factura.getEstado().equalsIgnoreCase("A")) {
      response.put("clave", "");
      response.put("consecutivo", "");
      response.put("fechaEmision", "");
      response.put("fileXmlSign", "");
      response.put("msj", "Esta factura ya esta aplicada, no puede volverla a aplicar.");
      response.put("response", Integer.valueOf(201));
      return new ResponseEntity(response, HttpStatus.CREATED);
    } 
    FEFacturasCXC cxc = new FEFacturasCXC();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    Long empresaId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
    Emisor emisor = this._emisorService.findById(empresaId);
    String medioPago = "";
    String tipoDocumento = request.getParameter("tipoDocumento");
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    CloseableHttpClient client = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost(this.apiJmataRecepcion);
    CloseableHttpResponse responseApi = null;
    HttpEntity entity2 = null;
    ObjectMapper objectMapper = null;
    JsonNode data = null;
    try {
      String j = "";
      String m = "";
      j = j + "{";
      j = j + "\"emisor\":\"" + emisor.getIdentificacion() + "\",";
      j = j + "\"tokenAccess\":\"" + emisor.getTokenAccess() + "\",";
      String codigoActividad = "";
      if (request.getParameter("codigoActividadEmisor") != null && request
        .getParameter("codigoActividadEmisor").length() > 0) {
        codigoActividad = request.getParameter("codigoActividadEmisor");
      } else {
        codigoActividad = request.getParameter("codigoActividadEmisor");
      } 
      j = j + "\"codigoActividadEmisor\":\"" + codigoActividad + "\",";
      j = j + "\"tipoDocumento\":\"" + tipoDocumento + "\",";
      j = j + "\"situacion\":\"" + request.getParameter("situacion") + "\",";
      j = j + "\"sucursal\":\"" + session.getAttribute("SESSION_NUMERO_SUCURSAL").toString() + "\",";
      j = j + "\"terminal\":\"" + session.getAttribute("SESSION_NUMERO_TERMINAL").toString() + "\",";
      j = j + "\"omitirReceptor\":\"" + request.getParameter("omitirReceptor") + "\",";
      factura.setEmisor(emisor);
      factura.setFechaEmisionFe(new Date());
      factura.setSituacion("normal");
      factura.setTipoDocumento(request.getParameter("tipoDocumento"));
      CSucursal sucursal = this._sucursalService.findById(Long.valueOf(Long.parseLong(session.getAttribute("SESSION_SUCURSAL_ID").toString())));
      factura.setSucursal(sucursal);
      CTerminal terminal = this._terminalService.findById(Long.valueOf(Long.parseLong(session.getAttribute("SESSION_TERMINAL_ID").toString())));
      factura.setTerminal(terminal);
      if (factura.getNumeroFactura() == null) {
        this.log.info("numero de factura esto fue lo ultimo que hice ");
        FEFactura numeroFactura = this._facturaService.findMaxFacturaByEmisor(empresaId);
        if (numeroFactura != null) {
          factura.setNumeroFactura(Long.valueOf(numeroFactura.getNumeroFactura().longValue() + 1L));
        } else {
          factura.setNumeroFactura(Long.valueOf(Long.parseLong("1")));
        } 
      } 
      Usuario usuario = this._usuarioService.findByUsername(authentication.getName());
      factura.setUsuario(usuario);
      if (request.getParameter("omitirReceptor").equalsIgnoreCase("false") && request.getParameter("cliente") != null && request.getParameter("cliente").length() > 0) {
        Cliente c = this._clienteService.findByIdByUserId(Long.valueOf(Long.parseLong(request.getParameter("cliente"))), empresaId, "C");
        cxc.setCliente(c);
        factura.setCliente(c);
        j = j + "\"receptorNombre\":\"" + procesarTexto(c.getNombreCompleto()) + "\",";
        j = j + "\"receptorTipoIdentif\":\"" + c.getTipoDeIdentificacion().getId() + "\",";
        j = j + "\"receptorNumIdentif\":\"" + c.getIdentificacion() + "\",";
        if (c.getTipoDeIdentificacion().getId().longValue() == 5L) {
          j = j + "\"receptorOtrasSenas\":\"" + procesarTexto(c.getOtrasSenas()) + "\",";
        } else if (c.getProvincia() != null && c.getCanton() != null && c.getDistrito() != null && c
          .getBarrio() != null) {
          j = j + "\"receptorProvincia\":\"" + c.getProvincia().getId() + "\",";
          j = j + "\"receptorCanton\":\"" + c.getCanton().getNumero_canton() + "\",";
          j = j + "\"receptorDistrito\":\"" + c.getDistrito().getNumeroDistrito() + "\",";
          j = j + "\"receptorBarrio\":\"" + c.getBarrio().getNumeroBarrio() + "\",";
          if (c.getOtrasSenas() != null)
            j = j + "\"receptorOtrasSenas\":\"" + procesarTexto(c.getOtrasSenas()) + "\","; 
        } 
        j = j + "\"receptorCodPaisTel\":\"" + c.getCodigoPais() + "\",";
        j = j + "\"receptorTel\":\"" + c.getTelefono1() + "\",";
        if (c.getFax() != null && c.getFax().length() > 0) {
          j = j + "\"receptorCodPaisFax\":\"506\",";
          j = j + "\"receptorFax\":\"" + c.getFax() + "\",";
        } 
        if (!c.getCorreo1().equalsIgnoreCase(correoCliente))
          try {
            this._clienteService.updateCorreo(correoCliente, c.getId());
          } catch (Exception exception) {} 
        j = j + "\"receptorEmail\":\"" + procesarTexto(correoCliente) + "\",";
      } else if (request.getParameter("inputClienteContado") != null && 
        !request.getParameter("inputClienteContado").equals("")) {
        j = j + "\"receptorNombre\":\"" + request.getParameter("inputClienteContado") + "\",";
      } 
      j = j + "\"condVenta\":\"" + this._funcionesService.str_pad(request.getParameter("condVenta"), 2, "0", "STR_PAD_LEFT") + "\",";
      j = j + "\"plazoCredito\":\"" + request.getParameter("plazoCredito") + "\",";
      if ((request.getParameter("medioPago1") != null && Double.parseDouble(request.getParameter("medioPago1")) > 0.0D) || (request
        
        .getParameter("medioPagoDolar1") != null && 
        Double.parseDouble(request.getParameter("medioPagoDolar1")) > 0.0D) || (request
        .getParameter("medioPagoEuro1") != null && 
        Double.parseDouble(request.getParameter("medioPagoEuro1")) > 0.0D)) {
        j = j + "\"medioPago\":\"" + this._funcionesService.str_pad("1", 2, "0", "STR_PAD_LEFT") + "\",";
        medioPago = "1";
      } else if ((request.getParameter("medioPago2") != null && 
        Double.parseDouble(request.getParameter("medioPago2")) > 0.0D) || (request
        .getParameter("medioPagoDolar2") != null && 
        Double.parseDouble(request.getParameter("medioPagoDolar2")) > 0.0D) || (request
        .getParameter("medioPagoEuro2") != null && 
        Double.parseDouble(request.getParameter("medioPagoEuro2")) > 0.0D)) {
        j = j + "\"medioPago\":\"" + this._funcionesService.str_pad("2", 2, "0", "STR_PAD_LEFT") + "\",";
        medioPago = "2";
      } else if ((request.getParameter("medioPago3") != null && 
        Double.parseDouble(request.getParameter("medioPago3")) > 0.0D) || (request
        .getParameter("medioPagoDolar3") != null && 
        Double.parseDouble(request.getParameter("medioPagoDolar3")) > 0.0D) || (request
        .getParameter("medioPagoEuro3") != null && 
        Double.parseDouble(request.getParameter("medioPagoEuro3")) > 0.0D)) {
        j = j + "\"medioPago\":\"" + this._funcionesService.str_pad("3", 2, "0", "STR_PAD_LEFT") + "\",";
        medioPago = "3";
      } else if ((request.getParameter("medioPago4") != null && 
        Double.parseDouble(request.getParameter("medioPago4")) > 0.0D) || (request
        .getParameter("medioPagoDolar4") != null && 
        Double.parseDouble(request.getParameter("medioPagoDolar4")) > 0.0D) || (request
        .getParameter("medioPagoEuro4") != null && 
        Double.parseDouble(request.getParameter("medioPagoEuro4")) > 0.0D)) {
        j = j + "\"medioPago\":\"" + this._funcionesService.str_pad("4", 2, "0", "STR_PAD_LEFT") + "\",";
        medioPago = "4";
      } else {
        j = j + "\"medioPago\":\"" + this._funcionesService.str_pad("1", 2, "0", "STR_PAD_LEFT") + "\",";
        medioPago = "1";
      } 
      Moneda moneda = this._monedaService.findById(Long.valueOf(Long.parseLong(request.getParameter("codMoneda"))));
      j = j + "\"detalleLinea\":{";
      int linea = 0;
      String naturalezaDescuento = "";
      for (int i = 0; i < itemId.length; i++) {
        String detalleProductoFinal;
        linea++;
        Double montoTotal = _montoTotal[i];
        Double descuento = Double.valueOf((descuentoTotal[i].toString() != null) ? 
            Double.parseDouble(descuentoTotal[i].toString()) : 0.0D);
        Double subTotalLinea = subTotal[i];
        try {
          detalleProductoFinal = detalleProducto[i].toString();
        } catch (Exception e) {
          detalleProductoFinal = "";
        } 
        if (descuento.doubleValue() > 0.0D)
          naturalezaDescuento = "Cliente Frecuente."; 
        FEFacturaItem item = new FEFacturaItem();
        Producto producto = this._productoService.findById(Long.valueOf(Long.parseLong(itemId[i].toString())));
        try {
          item.setId(idLinea[i]);
        } catch (Exception e) {
          this.log.info("Es una nueva linea de la factura");
        } 
        item.setNumeroLinea(linea);
        item.setProducto(producto);
        item.setDetalleProducto(detalleProductoFinal);
        item.setCantidad(Double.valueOf(Double.parseDouble(cantidad[i].toString())));
        item.setPrecioUnitario(Double.valueOf(Double.parseDouble(precioUnitario[i].toString())));
        item.setMontoTotal(Double.valueOf(Double.parseDouble(montoTotal.toString())));
        item.setSubTotal(subTotalLinea);
        item.setMontoDescuento(Double.valueOf(Double.parseDouble(descuento.toString())));
        item.setNaturalezaDescuento(naturalezaDescuento);
        item.setMontoTotalLinea(Double.valueOf(Double.parseDouble(totalLinea[i].toString())));
        item.setImpuestoNeto(Double.valueOf(Double.parseDouble(impuestoNeto[i].toString())));
        m = m + "\"" + linea + "\":{";
        m = m + "\"numeroLinea\":\"" + linea + "\",";
        try {
          m = m + "\"partidaArancelaria\":\"" + partidaArancelaria[i].toString() + "\",";
        } catch (Exception exception) {}
        m = m + "\"codigoComercial\":{";
        m = m + "\"0\":{";
        m = m + "\"tipo\":\"" + this._funcionesService.str_pad(producto.getTipoProducto().getId().toString(), 2, "0", "STR_PAD_LEFT") + "\",";
        m = m + "\"codigo\":\"" + producto.getCodigo() + "\"";
        m = m + "}";
        m = m + "},";
        m = m + "\"cantidad\":\"" + procesarNumeros(cantidad[i].toString(), "#0.00") + "\",";
        m = m + "\"unidadMedida\":\"" + unidadDeMedida[i].toString() + "\",";
        m = m + "\"detalle\":\"" + procesarTexto(nombreProducto[i].toString() + detalleProductoFinal) + "\",";
        m = m + "\"precioUnitario\":\"" + procesarNumeros(precioUnitario[i].toString(), "#0.00") + "\",";
        m = m + "\"montoTotal\":\"" + procesarNumeros(montoTotal.toString(), "#0.00") + "\",";
        m = m + "\"descuentos\":{";
        m = m + "\"0\":{";
        m = m + "\"montoDescuento\":\"" + procesarNumeros(descuento.toString(), "#0.00") + "\",";
        m = m + "\"naturalezaDescuento\":\"" + procesarTexto(naturalezaDescuento) + "\"";
        m = m + "}";
        m = m + "},";
        m = m + "\"subTotal\":\"" + procesarNumeros(subTotalLinea.toString(), "#0.00") + "\",";
        String[] impuestosId = request.getParameterValues("impuestosId" + itemId[i] + "[]");
        String[] Impuestoimpuestos = request.getParameterValues("Impuestoimpuestos" + itemId[i] + "[]");
        String[] impuestosMonto = request.getParameterValues("impuestosMonto" + itemId[i] + "[]");
        String[] checkBoxExonera = request.getParameterValues("exoneraCheckBox" + itemId[i] + "[]");
        String exoneraTipoDocumento = request.getParameter("exoneraTipoDocumentoExonera");
        String exoneraNumero = request.getParameter("exoneraNumero");
        String exoneraInstitucion = request.getParameter("exoneraInstitucion");
        String exoneraFechaEmision = request.getParameter("exoneraFechaEmision");
        String[] montoExoneracion = request.getParameterValues("montoExoneracion" + itemId[i] + "[]");
        String[] exoneraPorcentajeExoneracion = request.getParameterValues("exoneraPorcentajeExoneracion" + itemId[i] + "[]");
        if (impuestosId != null && impuestosId.length > 0) {
          String coma = "";
          m = m + "\"impuestos\":{";
          for (int q = 0; q < impuestosId.length; q++) {
            FEImpuestosItemFactura iif = new FEImpuestosItemFactura();
            try {
              String[] idLineaImpuesto = request.getParameterValues("idLineaImpuesto" + idLinea[i] + "[]");
              iif.setId(Long.valueOf(Long.parseLong(idLineaImpuesto[q])));
            } catch (Exception e) {
              this.log.info("Es un nueva linea de impuesto");
            } 
            CImpuesto impuesto = this._impuestoService.findById(Long.valueOf(Long.parseLong(impuestosId[q])));
            iif.setImpuesto(impuesto);
            CCodigosTarifasIva tiva = this._tarifasIvaService.findById(Long.valueOf(Long.parseLong(impuestoTarifaIva[q].substring(1, 2))));
            iif.setCodigoTarifaIva(tiva);
            iif.setTarifa(Double.valueOf(Double.parseDouble(Impuestoimpuestos[q])));
            iif.setMonto(Double.valueOf(Double.parseDouble(impuestosMonto[q])));
            if (q + 1 == impuestosId.length) {
              coma = "";
            } else {
              coma = ",";
            } 
            m = m + "\"" + q + "\":{";
            m = m + "\"codigo\":\"" + impuestosId[q] + "\",";
            m = m + "\"codigoTarifa\":\"" + impuestoTarifaIva[q] + "\",";
            m = m + "\"tarifa\":\"" + procesarNumeros(Impuestoimpuestos[q], "#0.00") + "\",";
            m = m + "\"monto\":\"" + procesarNumeros(impuestosMonto[q], "#0.00") + "\"";
            if (!tipoDocumento.equals("FEE"))
              try {
                if (checkBoxExonera[q] != null && !checkBoxExonera[q].toString().equals("") && checkBoxExonera[q]
                  .length() > 0) {
                  m = m + ",\"exoneracion\":{";
                  if (exoneraTipoDocumento != null && exoneraTipoDocumento.length() > 0)
                    m = m + "\"tipoDocumento\":\"" + exoneraTipoDocumento + "\","; 
                  if (exoneraNumero != null && exoneraNumero.length() > 0)
                    m = m + "\"numeroDocumento\":\"" + exoneraNumero + "\","; 
                  if (exoneraInstitucion != null && exoneraInstitucion.length() > 0)
                    m = m + "\"nombreInstitucion\":\"" + exoneraInstitucion + "\","; 
                  if (exoneraFechaEmision != null && exoneraFechaEmision.length() > 0)
                    m = m + "\"fechaEmision\":\"" + format.format((new SimpleDateFormat("dd/MM/yyyy"))
                        .parse(exoneraFechaEmision.toString())) + "\","; 
                  if (montoExoneracion[q] != null && montoExoneracion[q].length() > 0)
                    m = m + "\"montoExoneracion\":\"" + procesarNumeros(montoExoneracion[q], "#0.00") + "\","; 
                  if (exoneraPorcentajeExoneracion[q] != null && exoneraPorcentajeExoneracion[q]
                    .length() > 0)
                    m = m + "\"porcentajeExoneracion\":\"" + exoneraPorcentajeExoneracion[q] + "\""; 
                  m = m + "}";
                  FEExoneracionImpuestoItemFactura eiif = new FEExoneracionImpuestoItemFactura();
                  eiif.setTipoDocumento(exoneraTipoDocumento);
                  eiif.setNumeroDocumento(exoneraNumero);
                  eiif.setNombreInstitucion(exoneraInstitucion);
                  eiif.setFechaEmision(exoneraFechaEmision);
                  eiif.setMontoImpuesto(Double.valueOf(Double.parseDouble(montoExoneracion[q])));
                  eiif.setPorcentajeCompra(Integer.parseInt(exoneraPorcentajeExoneracion[q]));
                  iif.addItemFacturaImpuestosExoneracion(eiif);
                } 
              } catch (Exception exception) {} 
            item.addFEImpuestosItemFactura(iif);
            m = m + "}" + coma;
          } 
          m = m + "},";
        } 
        m = m + "\"impuestoNeto\":\"" + procesarNumeros(impuestoNeto[i].toString(), "#0.00") + "\",";
        m = m + "\"montoTotalLinea\":\"" + procesarNumeros(totalLinea[i].toString(), "#0.00") + "\"";
        m = m + "},";
        factura.addFEItemFactura(item);
      } 
      j = j + m.substring(0, m.length() - 1);
      j = j + "},";
      String[] otrosCargosTipoDoc = request.getParameterValues("otrosCargosTipoDoc[]");
      String[] otrosCargosIdentificacion = request.getParameterValues("otrosCargosIdentificacion[]");
      String[] otrosCargosNombre = request.getParameterValues("otrosCargosNombre[]");
      String[] otrosCargosDetalle = request.getParameterValues("otrosCargosDetalle[]");
      String[] otrosCargosPorcentaje = request.getParameterValues("otrosCargosPorcentaje[]");
      String[] otrosCargosMonto = request.getParameterValues("otrosCargosMonto[]");
      String oc = "";
      if (otrosCargosTipoDoc != null && otrosCargosTipoDoc.length > 0) {
        oc = oc + "\"otrosCargos\":{";
        String coma2 = "";
        for (int k = 0; k < otrosCargosTipoDoc.length; k++) {
          if (k + 1 == otrosCargosTipoDoc.length) {
            coma2 = "";
          } else {
            coma2 = ",";
          } 
          oc = oc + "\"" + k + "\":{";
          oc = oc + "\"tipoDocumento\":\"" + otrosCargosTipoDoc[k].toString() + "\",";
          oc = oc + "\"numeroIdentidadTercero\":\"" + otrosCargosIdentificacion[k].toString() + "\",";
          oc = oc + "\"nombreTercero\":\"" + otrosCargosNombre[k].toString() + "\",";
          oc = oc + "\"detalle\":\"" + otrosCargosDetalle[k].toString() + "\",";
          oc = oc + "\"porcentaje\":\"" + procesarTexto(otrosCargosPorcentaje[k].toString()) + "\",";
          oc = oc + "\"montoCargo\":\"" + procesarNumeros(otrosCargosMonto[k].toString(), "#0.00") + "\"";
          oc = oc + "}" + coma2;
          FEFacturaOtrosCargos foc = new FEFacturaOtrosCargos();
          foc.setTipoDocumento(otrosCargosTipoDoc[k].toString());
          foc.setNumeroIdentidadTercero(otrosCargosIdentificacion[k].toString());
          foc.setNombreTercero(otrosCargosNombre[k].toString());
          foc.setDetalle(procesarTexto(otrosCargosDetalle[k].toString()));
          foc.setPorcentaje(procesarTexto(otrosCargosPorcentaje[k].toString()));
          foc.setMontoCargo(procesarNumeros(otrosCargosMonto[k].toString(), "#0.00"));
          factura.addOtrosCargos(foc);
        } 
        oc = oc + "},";
      } 
      j = j + oc;
      String[] referenciaTipoDoc = request.getParameterValues("referenciaTipoDoc[]");
      String[] referenciaClaveDocumento = request.getParameterValues("referenciaClaveDocumento[]");
      String[] referenciaFechaDeEmision = request.getParameterValues("referenciaFechaDeEmision[]");
      String[] referenciaCodigoDocumento = request.getParameterValues("referenciaCodigoDocumento[]");
      String[] referenciaRazonDocumento = request.getParameterValues("referenciaRazonDocumento[]");
      String r = "";
      if (referenciaClaveDocumento != null && referenciaClaveDocumento.length > 0) {
        r = r + "\"referencias\":{";
        String coma2 = "";
        for (int k = 0; k < referenciaClaveDocumento.length; k++) {
          if (k + 1 == referenciaClaveDocumento.length) {
            coma2 = "";
          } else {
            coma2 = ",";
          } 
          String fechaReferencia = formatDate.format((new SimpleDateFormat("dd/MM/yyyy")).parse(referenciaFechaDeEmision[k].toString())) + "-06:00";
          r = r + "\"" + k + "\":{";
          r = r + "\"tipoDoc\":\"" + referenciaTipoDoc[k].toString() + "\",";
          r = r + "\"numero\":\"" + referenciaClaveDocumento[k].toString() + "\",";
          r = r + "\"fechaEmision\":\"" + fechaReferencia + "\",";
          r = r + "\"codigo\":\"" + referenciaCodigoDocumento[k].toString() + "\",";
          r = r + "\"razon\":\"" + procesarTexto(referenciaRazonDocumento[k].toString()) + "\"";
          r = r + "}" + coma2;
          FEFacturaReferencia fr = new FEFacturaReferencia();
          CCodigoReferencia codigoReferencia = this._codigoReferenciaService.findById(Long.valueOf(Long.parseLong(referenciaCodigoDocumento[k].toString())));
          fr.setTipoDoc(referenciaClaveDocumento[k].toString().substring(29, 31));
          fr.setNumero(referenciaClaveDocumento[k].toString());
          fr.setFechaEmision(fechaReferencia);
          fr.setCodigoReferencia(codigoReferencia);
          fr.setRazon(referenciaRazonDocumento[k].toString());
          factura.addReferenciaFactura(fr);
        } 
        r = r + "},";
      } 
      j = j + r;
      j = j + "\"codMoneda\":\"" + moneda.getSimboloMoneda() + "\",";
      j = j + "\"tipoCambio\":\"" + procesarNumeros(request.getParameter("tipoCambio"), "#0.00") + "\",";
      j = j + "\"totalServGravados\":\"" + procesarNumeros(request.getParameter("totalServiciosGravados"), "#0.00") + "\",";
      j = j + "\"totalServExentos\":\"" + procesarNumeros(request.getParameter("totalServiciosExentos"), "#0.00") + "\",";
      if (!tipoDocumento.equals("FEE"))
        j = j + "\"totalServExonerado\":\"" + procesarNumeros(request.getParameter("totalServiciosExonerado"), "#0.00") + "\","; 
      j = j + "\"totalMercGravadas\":\"" + procesarNumeros(request.getParameter("totalMercanciaGravadas"), "#0.00") + "\",";
      j = j + "\"totalMercExentas\":\"" + procesarNumeros(request.getParameter("totalMercanciasExentas"), "#0.00") + "\",";
      if (!tipoDocumento.equals("FEE"))
        j = j + "\"totalMercExonerada\":\"" + procesarNumeros(request.getParameter("totalMercanciasExonerada"), "#0.00") + "\","; 
      j = j + "\"totalGravados\":\"" + procesarNumeros(request.getParameter("totalGravados"), "#0.00") + "\",";
      j = j + "\"totalExentos\":\"" + procesarNumeros(request.getParameter("totalExentos"), "#0.00") + "\",";
      j = j + "\"totalExonerado\":\"" + procesarNumeros(request.getParameter("totalExonerado"), "#0.00") + "\",";
      j = j + "\"totalVentas\":\"" + procesarNumeros(request.getParameter("totalVenta"), "#0.00") + "\",";
      j = j + "\"totalDescuentos\":\"" + procesarNumeros(request.getParameter("descuentos"), "#0.00") + "\",";
      j = j + "\"totalVentasNeta\":\"" + procesarNumeros(request.getParameter("totalVentaNeta"), "#0.00") + "\",";
      j = j + "\"totalImp\":\"" + procesarNumeros(request.getParameter("totalImpuestos"), "#0.00") + "\",";
      if (!tipoDocumento.equals("FEE") && !tipoDocumento.equals("FEC"))
        j = j + "\"totalIVADevuelto\":\"" + procesarNumeros(request.getParameter("totalIVADevuelto"), "#0.00") + "\","; 
      j = j + "\"totalOtrosCargos\":\"" + procesarNumeros(request.getParameter("totalOtrosCargos"), "#0.00") + "\",";
      j = j + "\"totalComprobante\":\"" + procesarNumeros(request.getParameter("totalFactura"), "#0.00") + "\",";
      j = j + "\"otros\":\"" + procesarTexto(request.getParameter("notaFactura")) + "\",";
      j = j + "\"numeroFactura\":\"" + factura.getNumeroFactura() + "\"";
      j = j + "}";
      if (!tipoDocumento.equalsIgnoreCase("PR")) {
        this.log.info("JSON GENERADO ===================> " + j);
        String json = j;
        StringEntity entity = new StringEntity(json, "UTF-8");
        httpPost.setEntity((HttpEntity)entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        responseApi = client.execute((HttpUriRequest)httpPost);
        entity2 = responseApi.getEntity();
        String responseString = EntityUtils.toString(entity2, "UTF-8");
        objectMapper = new ObjectMapper();
        data = objectMapper.readTree(responseString);
        this.log.info("AHORA __________________________ " + data);
      } 
      factura.setCondVenta(request.getParameter("condVenta"));
      factura.setClienteContado(request.getParameter("inputClienteContado"));
      factura.setPlazoCredito(request.getParameter("plazoCredito"));
      factura.setMedioPago(medioPago);
      factura.setMoneda(moneda);
      factura.setTipoCambio(Double.valueOf(Double.parseDouble(request.getParameter("tipoCambio"))));
      factura.setTipoCambioDolar(Double.valueOf(Double.parseDouble(request.getParameter("tp-2"))));
      factura.setTipoCambioEuro(Double.valueOf(Double.parseDouble(request.getParameter("tp-3"))));
      factura.setTotalServGravados(Double.valueOf(Double.parseDouble(request.getParameter("totalServiciosGravados"))));
      factura.setTotalServExentos(Double.valueOf(Double.parseDouble(request.getParameter("totalServiciosExentos"))));
      factura.setTotalServExonerado(Double.valueOf(Double.parseDouble(request.getParameter("totalServiciosExonerado"))));
      factura.setTotalMercGravadas(Double.valueOf(Double.parseDouble(request.getParameter("totalMercanciaGravadas"))));
      factura.setTotalMercExentas(Double.valueOf(Double.parseDouble(request.getParameter("totalMercanciasExentas"))));
      factura.setTotalMercExonerada(Double.valueOf(Double.parseDouble(request.getParameter("totalMercanciasExonerada"))));
      factura.setTotalGravados(Double.valueOf(Double.parseDouble(request.getParameter("totalGravados"))));
      factura.setTotalExentos(Double.valueOf(Double.parseDouble(request.getParameter("totalExentos"))));
      factura.setTotalExonerado(Double.valueOf(Double.parseDouble(request.getParameter("totalExonerado"))));
      factura.setTotalVentas(Double.valueOf(Double.parseDouble(request.getParameter("totalVenta"))));
      factura.setTotalDescuentos(Double.valueOf(Double.parseDouble(request.getParameter("descuentos"))));
      factura.setTotalVentaNeta(Double.valueOf(Double.parseDouble(request.getParameter("totalVentaNeta"))));
      factura.setTotalImp(Double.valueOf(Double.parseDouble(request.getParameter("totalImpuestos"))));
      factura.setTotalIVADevuelto(Double.valueOf(Double.parseDouble(request.getParameter("totalIVADevuelto"))));
      factura.setTotalOtrosCargos(Double.valueOf(Double.parseDouble(request.getParameter("totalOtrosCargos"))));
      factura.setTotalComprobante(Double.valueOf(Double.parseDouble(request.getParameter("totalFactura"))));
      factura.setOtros(request.getParameter("notaFactura"));
      factura.setNumeroTarjeta(procesarTexto(request.getParameter("numeroTarjeta")));
      factura.setNumeroAutorizacion(procesarTexto(request.getParameter("numeroAutorizacion")));
      if (tipoDocumento.equalsIgnoreCase("PR")) {
        factura.setClave("");
        factura.setFechaEmision(format.format(new Date()));
        factura.setConsecutivo("");
      } else {
        factura.setClave(data.path("clave").asText());
        factura.setFechaEmision(data.path("fechaEmision").asText());
        factura.setConsecutivo(data.path("consecutivo").asText());
      } 
      factura.setEfectivo(
          Double.valueOf((request.getParameter("medioPago1") != null && request.getParameter("medioPago1").length() > 0) ? 
            Double.parseDouble(request.getParameter("medioPago1")) : 0.0D));
      factura.setTarjeta(
          Double.valueOf((request.getParameter("medioPago2") != null && request.getParameter("medioPago2").length() > 0) ? 
            Double.parseDouble(request.getParameter("medioPago2")) : 0.0D));
      factura.setCheque(
          Double.valueOf((request.getParameter("medioPago3") != null && request.getParameter("medioPago3").length() > 0) ? 
            Double.parseDouble(request.getParameter("medioPago3")) : 0.0D));
      factura.setTransferencia(
          Double.valueOf((request.getParameter("medioPago4") != null && request.getParameter("medioPago4").length() > 0) ? 
            Double.parseDouble(request.getParameter("medioPago4")) : 0.0D));
      factura.setTipoTarjeta(
          (request.getParameter("tipoTarjeta") != null) ? request.getParameter("tipoTarjeta") : "A");
      factura.setSuVueltro(
          Double.valueOf((request.getParameter("suVuelto") != null && request.getParameter("suVuelto").length() > 0) ? 
            Double.parseDouble(request.getParameter("suVuelto")) : 0.0D));
      if (tipoDocumento.equalsIgnoreCase("PR")) {
        factura.setEstado("P");
      } else {
        factura.setEstado("A");
        FEBitacora feb = new FEBitacora();
        feb.setClave(data.path("clave").asText());
        feb.setTipoDocumento(request.getParameter("tipoDocumento"));
        feb.setResponseCode(data.path("response").asInt());
        feb.setNameXmlEnviado(data.path("fileXmlSign").asText());
        feb.setFechaEmision(data.path("fechaEmision").asText());
        factura.addDocumentoABitacora(feb);
      } 
      this._facturaService.save(factura);
      if (tipoDocumento.equalsIgnoreCase("PR")) {
        response.put("clave", "");
        response.put("consecutivo", "");
        response.put("fechaEmision", "");
        response.put("fileXmlSign", "");
        response.put("msj", "");
        response.put("factura_id", factura.getId());
        response.put("response", Integer.valueOf(200));
      } else {
        if (request.getParameter("condVenta") != null && request.getParameter("condVenta").equals("2")) {
          cxc.setNumeroFactura(factura.getNumeroFactura());
          cxc.setTotalDeuda(Double.parseDouble(request.getParameter("totalFactura")));
          cxc.setMoneda(factura.getMoneda().getSimboloMoneda());
          cxc.setClave(factura.getClave());
          cxc.setEmisor(emisor);
          cxc.setFechaEmisionFe(factura.getFechaEmisionFe());
          cxc.setUsuario(usuario);
          cxc.setPlazoCredito(factura.getPlazoCredito());
          cxc.setEstadoPago("A");
          this._cxcService.save(cxc);
        } 
        for (int k = 0; k < itemId.length; k++) {
          Producto producto = this._productoService.findById(Long.valueOf(Long.parseLong(itemId[k].toString())));
          if (producto.getAfectaInventario().equals("1"))
            if (tipoDocumento.equalsIgnoreCase("NC")) {
              this._productoService.registroEntradasInvent(Double.valueOf(Double.parseDouble(cantidad[k].toString())), producto
                  .getId());
            } else if (!tipoDocumento.equalsIgnoreCase("FEC")) {
              this._productoService.registroSalidasInvent(Double.valueOf(Double.parseDouble(cantidad[k].toString())), producto
                  .getId());
            }  
        } 
        response.put("clave", data.path("clave").asText());
        response.put("consecutivo", data.path("consecutivo").asText());
        response.put("fechaEmision", data.path("fechaEmision").asText());
        response.put("fileXmlSign", data.path("fileXmlSign").asText());
        response.put("msj", "");
        response.put("response", Integer.valueOf(200));
      } 
    } catch (Exception e) {
      response.put("clave", "");
      response.put("consecutivo", "");
      response.put("fechaEmision", "");
      response.put("fileXmlSign", "");
      response.put("msj", procesarTexto(e.getMessage()));
      response.put("response", Integer.valueOf(401));
      e.printStackTrace();
    } finally {
      if (responseApi != null)
        responseApi.close(); 
      entity2 = null;
      objectMapper = null;
      data = null;
    } 
    return new ResponseEntity(response, HttpStatus.CREATED);
  }
  
  @PostMapping(value = {"/enviar-proforma"}, produces = {"application/json"})
  public ResponseEntity<?> enviarProforma(@RequestParam(name = "correo", required = false) String email, @RequestParam(name = "factura", required = true) Long facturaId, HttpSession session) throws JRException, IOException, SQLException, MessagingException {
    Map<String, Object> response = new HashMap<>();
    String emailEnviar = "";
    Connection db = this.dataSource.getConnection();
    InputStream reportfile = getClass().getResourceAsStream("/proforma/facturas.jasper");
    URL base = getClass().getResource("/proforma/");
    String baseUrl = base.toString();
    try {
      if (facturaId != null && facturaId.longValue() > 0L) {
        Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
        FEFactura factura = this._facturaService.findByEmisorById(emisorId, facturaId);
        if (factura != null) {
          System.out.println("correo " + factura.getCliente().getCorreo1());
          if (email != null && email.length() > 0) {
            emailEnviar = email;
          } else {
            emailEnviar = factura.getCliente().getCorreo1();
          } 
          Emisor e = this._emisorService.findById(emisorId);
          String td = tipoDocumento(factura.getTipoDocumento());
          String logo = e.getLogoEmpresa();
          if (logo != null && !logo.equals("") && logo.length() > 0) {
            logo = this.pathUploadFilesApi + "logo/" + logo;
          } else {
            logo = this.pathUploadFilesApi + "logo/default.png";
          } 
          Map<String, Object> parameter = new HashMap<>();
          parameter.put("BASE_URL", baseUrl);
          parameter.put("BASE_URL_LOGO", logo);
          parameter.put("CLAVE_FACTURA", factura.getId());
          parameter.put("TIPO_DOCUMENTO", tipoDocumento(factura.getTipoDocumento()));
          parameter.put("RESOLUCION", factura.getEmisor().getNotaValidezProforma());
          parameter.put("NOTA_FACTURA", factura.getOtros());
          parameter.put("URL_QR", this.urlQr + factura.getId());
          byte[] bytes = JasperRunManager.runReportToPdf(reportfile, parameter, db);
          if (bytes != null && bytes.length > 0) {
            ByteArrayDataSource pdfBytes = new ByteArrayDataSource(bytes, "application/pdf");
            MimeMessage message = this.emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            String msj = null;
            String clienteFinal = (factura.getCliente() != null && factura.getCliente().getNombreCompleto().length() > 0) ? factura.getCliente().getNombreCompleto() : "";
            msj = "<p style=\"font-family: Arial;\">Estimado cliente,</p>";
            msj = msj + "<p style=\"font-family: Arial;\">Le hacemos llegar el comprobante de <b>" + td + "</b> con el nde consecutivo <b>" + factura.getNumeroFactura() + "</b>, generada por <b>" + e.getNombreRazonSocial() + "</b></p><p><b><i>" + e.getNotaValidezProforma() + "</i></b></p>";
            msj = msj + "<p style=\"font-family: Arial;\">Saludos,</p>";
            msj = msj + "<p style=\"font-family: Arial;\"><b>" + e.getNombreComercial() + "</b></p>";
            helper.setTo(new InternetAddress(emailEnviar, clienteFinal));
            helper.setReplyTo(e.getEmail(), e.getNombreComercial());
            helper.setFrom(new InternetAddress(this.correoDistribucion, td));
            helper.setSubject(td + " - " + e.getNombreComercial());
            helper.setText(msj, true);
            helper.addAttachment("factura-proforma-numero-" + factura.getNumeroFactura() + ".pdf", (DataSource)pdfBytes);
            this.emailSender.send(message);
            response.put("msj", "Se envun correo con la factura a: " + emailEnviar);
            response.put("response", Integer.valueOf(200));
            return new ResponseEntity(response, HttpStatus.CREATED);
          } 
          response.put("msj", "Error contacte al desarrollador del sistema.");
          response.put("response", Integer.valueOf(200));
          return new ResponseEntity(response, HttpStatus.CREATED);
        } 
        response.put("msj", "El documento que desea reenviar no existe.");
        response.put("response", Integer.valueOf(200));
        return new ResponseEntity(response, HttpStatus.CREATED);
      } 
      response.put("msj", "El nde factura es requerido!!!");
      response.put("response", Integer.valueOf(200));
      return new ResponseEntity(response, HttpStatus.CREATED);
    } catch (Exception ex) {
      response.put("msj", "Error al intentar enviar el correo a " + email + ", error generado: " + ex
          .getMessage());
      response.put("response", Integer.valueOf(204));
      return new ResponseEntity(response, HttpStatus.NO_CONTENT);
    } finally {
      reportfile.close();
      try {
        if (db != null)
          db.close(); 
      } catch (SQLException e) {
        System.out.println("Error: desconectando la base de datos.");
      } 
    } 
  }
  
  public String procesarNumeros(String j, String decimales) {
    NumberFormat formatter = new DecimalFormat(decimales);
    String r = "";
    r = (j != null && !j.equals("")) ? j : "0.00000";
    r = formatter.format(Double.parseDouble(r));
    r = r.replaceAll(",", ".");
    return r;
  }
  
  public String procesarTexto(String j) {
    String r = "";
    r = StringEscapeUtils.escapeJson(j);
    return r;
  }
  
  public String procesarTextoInput(String j) {
    String r = "";
    r = StringEscapeUtils.escapeJava(j);
    return r;
  }
  
  public String tipoDocumento(String td) {
    String resp = "";
    switch (td) {
      case "FE":
        resp = "Factura Electrónica";
        break;
      case "ND":
        resp = "Nota de debito Electrónica";
        break;
      case "NC":
        resp = "Nota de crédito Electrónica";
        break;
      case "TE":
        resp = "Tiquete Electrónico";
        break;
      case "FEC":
        resp = "Factura Electrónica de Compra";
        break;
      case "FEE":
        resp = "Factura Electrónica Exportación";
        break;
      case "PR":
        resp = "Proforma";
        break;
    }
    return resp;
  }
}
