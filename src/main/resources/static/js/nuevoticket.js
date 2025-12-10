function init(){
    $("#ticket_form").on("submit",function(e){
        guardaryeditar(e);
    });
}

$(document).ready(function() {

    // 1. INICIALIZAR SUMMERNOTE (Solo si existe el elemento para evitar errores)
    if ($('#tick_descrip').length > 0) {
        $('#tick_descrip').summernote({
            height: 150,
            lang: "es-ES",
            toolbar: [
                ['style', ['bold', 'italic', 'underline', 'clear']],
                ['font', ['strikethrough', 'superscript', 'subscript']],
                ['fontsize', ['fontsize']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['height', ['height']]
            ]
        });
    }

    // 2. CARGAR COMBO CATEGORÍA
    $.get("/api/categorias/combo", function(data) {
        // data ya es la lista filtrada de activos [...]
        $('#cat_id').empty().append('<option value="">Seleccionar</option>');
        $.each(data, function(index, value) {
            $('#cat_id').append('<option value="' + value.codigoCategoria + '">' + value.nombreCategoria + '</option>');
        });
        $('#cat_id').select2();
    }).fail(function() {
        console.error("Error al cargar categorías");
    });

    // 3. EVENTO CHANGE CATEGORÍA (Carga Subcategorías)
    $("#cat_id").change(function(){
        var cat_id = $(this).val();

        if (cat_id) {
            // Esta ruta ya está correcta en tu Controller: /api/subcategorias/por-categoria
            $.get("/api/subcategorias/por-categoria", { cat_id : cat_id }, function(data) {
                $('#cats_id').empty().append('<option value="">Seleccionar</option>');
                $.each(data, function(index, value) {
                    $('#cats_id').append('<option value="' + value.codigoSubCategoria + '">' + value.nombre + '</option>');
                });
                $('#cats_id').select2();
            });
        } else {
            $('#cats_id').empty().append('<option value="">Seleccionar</option>').select2();
        }
    });

    // 4. CARGAR COMBO PRIORIDAD (CORREGIDO)
    // Antes apuntaba a /api/prioridades (Tabla), ahora apunta a /api/prioridades/combo (Lista limpia)
    $.get("/api/prioridades/combo", function(data) {
        $('#prio_id').empty().append('<option value="">Seleccionar</option>');
        $.each(data, function(index, value) {
            $('#prio_id').append('<option value="' + value.codigoPrioridad + '">' + value.nombrePrioridad + '</option>');
        });
        $('#prio_id').select2();
    }).fail(function() {
        console.error("Error al cargar prioridades");
    });

    // Inicializar select2 de subcategoría vacío al principio
    $('#cats_id').select2();

});

function guardaryeditar(e){
    e.preventDefault();
    $('#btnguardar').prop("disabled",true);
    $('#btnguardar').html('<i class="fa fa-spinner fa-spin"></i> Espere..');

    var formData = new FormData($("#ticket_form")[0]);

    // Validación básica
    var descripcion = $('#tick_descrip').summernote('isEmpty');
    var titulo = $('#tick_titulo').val();
    var subcat = $('#cats_id').val();
    var cat = $('#cat_id').val();
    var prio = $('#prio_id').val();

    if (descripcion || titulo === '' || subcat === '' || cat === '' || prio === ''){
        swal("Advertencia!", "Campos Obligatorios Vacíos", "warning");
        $('#btnguardar').prop("disabled",false);
        $('#btnguardar').html('Guardar');
    } else {
        $.ajax({
            url: "/api/tickets/insertar",
            type: "POST",
            data: formData,
            contentType: false,
            processData: false,
            success: function(data){
                console.log(data);
                // Limpiar campos
                $('#tick_titulo').val('');
                $('#tick_descrip').summernote('reset');
                $('#cat_id').val('').trigger('change');
                $('#prio_id').val('').trigger('change');
                $('#fileElem').val(''); // Limpiar input file

                swal("Correcto!", "Ticket Registrado Correctamente: Nro-" + data.tick_id, "success");
                $('#btnguardar').prop("disabled",false);
                $('#btnguardar').html('Guardar');
            },
            error: function(jqXHR, textStatus, errorThrown){
                console.error("Error al guardar ticket:", jqXHR.responseText);
                swal("Error!", "No se pudo registrar el ticket.", "error");
                $('#btnguardar').prop("disabled",false);
                $('#btnguardar').html('Guardar');
            }
        });
    }
}

init();