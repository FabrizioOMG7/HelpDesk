var tabla;

function init(){
    // Evento del formulario del Modal Asignar
    $("#ticket_asignar_form").on("submit",function(e){
        asignar(e);
    });

    // Cargar listas desplegables al iniciar
    cargarCombosFiltros();
    cargarTecnicosModal();
}

// --- 1. CARGAR COMBOS PARA FILTROS ---
function cargarCombosFiltros() {
    // Categorías (Solo activas)
    $.get("/api/categorias/combo", function(data) {
        $('#cat_id').empty().append('<option value="">Seleccionar</option>');
        $.each(data, function(index, value) {
            $('#cat_id').append('<option value="' + value.codigoCategoria + '">' + value.nombreCategoria + '</option>');
        });
        $('#cat_id').select2();
    });

    // Prioridades (Solo activas)
    $.get("/api/prioridades", function(data) {
        // Manejar si devuelve {data:[]} o []
        var lista = data.data ? data.data : data;
        $('#prio_id').empty().append('<option value="">Seleccionar</option>');
        $.each(lista, function(index, value) {
            if(value.estado === true) {
                $('#prio_id').append('<option value="' + value.codigoPrioridad + '">' + value.nombrePrioridad + '</option>');
            }
        });
        $('#prio_id').select2();
    });
}

// --- 2. CARGAR TÉCNICOS PARA EL MODAL ---
function cargarTecnicosModal(){
    $.get("/api/consultarticket/tecnicos", function(data) {
        $('#usu_id_soporte').empty().append('<option value="">Seleccionar Técnico</option>');
        $.each(data, function(index, value) {
            // Concatenar Nombre y Apellido
            $('#usu_id_soporte').append('<option value="' + value.codigoUsuario + '">' + value.nombres + ' ' + value.apellidoPaterno + '</option>');
        });
        $('#usu_id_soporte').select2();
    });
}

$(document).ready(function(){

    // --- 3. INICIALIZAR DATATABLE ---
    tabla = $('#ticket_data').DataTable({
        "processing": true,
        "serverSide": false,
        dom: 'Bfrtip',
        "searching": true,
        lengthChange: false,
        colReorder: true,
        buttons: ['copyHtml5', 'excelHtml5', 'csvHtml5', 'pdfHtml5'],
        "ajax": {
            url: '/api/consultarticket',
            type: "GET",
            dataType: "json",
            // Enviamos los valores de los filtros al backend
            data: function(d) {
                d.tick_titulo = $('#tick_titulo').val();
                d.cat_id = $('#cat_id').val();
                d.prio_id = $('#prio_id').val();
            },
            dataSrc: "data",
            error: function(e){ console.error(e); }
        },
        "columns": [
            { "data": "codigoTicket" }, // Nro Ticket
            { "data": "titulo" },       // Titulo
            { "data": "nombreCategoria" },
            { "data": "nombrePrioridad" },
            { "data": "fechaCreacion" },
            {
                "data": "nombreSoporte",
                "className": "text-center",
                "render": function(data, type, row) {
                    // Lógica de colores y botón
                    if (row.soporteId === null) {
                        // Amarillo si no hay asignado
                        return '<button type="button" onClick="modalAsignar('+row.codigoTicket+')" class="btn btn-warning btn-sm">Sin Asignar</button>';
                    } else {
                        // Verde con el nombre si ya está asignado
                        return '<button type="button" onClick="modalAsignar('+row.codigoTicket+')" class="btn btn-success btn-sm">' + data + '</button>';
                    }
                }
            },
            { "data": "fechaAsignacion" }, // Fecha Asignación
            { "data": "fechaCierre" },
            {
                "data": null,
                "className": "text-center",
                "render": function(data, type, row) {
                    // Botón Ojito (Redirige al detalle)
                    return '<button type="button" class="btn btn-inline btn-primary btn-sm btn-icon" onClick="verDetalle('+row.codigoTicket+')"><i class="fa fa-eye"></i></button>';
                }
            }
        ],
        "language": {
            "url": "//cdn.datatables.net/plug-ins/1.10.21/i18n/Spanish.json"
        }
    });

    // --- 4. EVENTOS DE BOTONES DE FILTRO ---

    // Botón Filtrar
    $('#btnfiltrar').click(function(){
        // Al recargar, DataTable leerá automáticamente los inputs gracias a la función "data" definida arriba
        tabla.ajax.reload();
    });

    // Botón Ver Todo (Resetear)
    $('#btntodo').click(function(){
        // Limpiar inputs
        $('#tick_titulo').val('');
        $('#cat_id').val('').trigger('change'); // Resetear Select2
        $('#prio_id').val('').trigger('change'); // Resetear Select2

        // Recargar tabla limpia
        tabla.ajax.reload();
    });

});

// --- 5. FUNCIONES AUXILIARES ---

function modalAsignar(ticket_id){
    $('#tick_id').val(ticket_id);
    $('#mdltitulo').html('Asignar Agente');
    $('#modalasignar').modal('show');
}

function asignar(e){
    e.preventDefault();
    var formData = new FormData($("#ticket_asignar_form")[0]);
    var token = $("input[name='_csrf']").val();

    $.ajax({
        url: "/api/consultarticket/asignar",
        type: "POST",
        data: formData,
        contentType: false,
        processData: false,
        beforeSend: function(xhr) { xhr.setRequestHeader("X-CSRF-TOKEN", token); },
        success: function(datos){
            $('#ticket_asignar_form')[0].reset();
            $('#usu_id_soporte').val('').trigger('change');
            $("#modalasignar").modal('hide');

            // Recargar tabla para ver el cambio de color y fecha
            tabla.ajax.reload();

            swal({
                title: "HelpDesk!",
                text: "Ticket Asignado Correctamente.",
                type: "success",
                confirmButtonClass: "btn-success"
            });
        },
        error: function(e) {
            swal("Error", "No se pudo asignar el ticket", "error");
        }
    });
}

function verDetalle(ticket_id){
    // Redirige al módulo de detalle (por ahora Área como ejemplo)
    // window.location.href = '/detalle-ticket/' + ticket_id; // Idealmente sería esto
    window.location.href = '/mantenimiento/area';
}

init();