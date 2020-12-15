<template>
  <v-container>
    <v-select
      v-model="consultorio"
      :items="consultorios"
      item-value="id"
      item-text="nombre"
      label="Consultorio"
      placeholder="Seleccione un consultorio"
      outlined
      dense
      :menu-props="{ bottom: true, offsetY: true }"
      @change="getProductos()"
      v-if="user.role == 'ADMIN'"
    ></v-select>
    <v-toolbar color="azulSecundario" dark flat>
      <v-toolbar-title>Inventario</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-text-field
        v-model="buscar"
        append-icon="mdi-magnify"
        label="Buscar producto"
        single-line
        hide-details
      ></v-text-field>
    </v-toolbar>
    <v-simple-table class="tablaInventario bordeTabla" elevation="24">
      <template v-slot:default>
        <thead>
          <tr>
            <th class="text-left">
              Producto
            </th>
            <th class="text-left">
              Cantidad Actual
            </th>
            <th class="text-left">
              Cantidad Nueva
            </th>
          </tr>
        </thead>

        <tbody class="tablaInventario">
          <tr v-for="producto in filtrarPorNombre" :key="producto.id">
            <!-- <tr> -->
            <td>{{ producto.nombre }}</td>
            <!-- <td>Tornillo de Pancho</td> -->
            <td>{{ producto.cantidad }}</td>
            <td>
              <div class="contadores">
                <v-btn
                  v-if="producto.cantidadNueva > 0"
                  class="mx-2"
                  fab
                  dark
                  small
                  @click="producto.cantidadNueva--"
                  color="primary"
                >
                  <v-icon dark>
                    mdi-minus
                  </v-icon>
                </v-btn>

                <v-btn v-else class="mx-2" fab dark small color="primary">
                  <v-icon dark>
                    mdi-minus
                  </v-icon>
                </v-btn>

                <v-text-field
                  v-model="producto.cantidadNueva"
                  solo
                  class="campoNumero"
                  type="number"
                  min="0"
                  required
                  oninput="validity.valid||(value='');"
                ></v-text-field>

                <v-btn
                  class="mx-2"
                  fab
                  dark
                  small
                  color="primary"
                  @click="producto.cantidadNueva++"
                >
                  <v-icon dark>
                    mdi-plus
                  </v-icon>
                </v-btn>
              </div>
            </td>
          </tr>
        </tbody>
      </template>
    </v-simple-table>
    <div v-if="!consultorio" class="sinConsultorio" colspan="4">
      Debes seleccionar un consultorio.
    </div>

    <div
      v-if="filtrarPorNombre.length == 0 && consultorio != null"
      class="sinConsultorio"
      colspan="4"
    >
      Este consultorio no tiene nada en el inventario o el producto que estas
      buscando no existe.
    </div>

    <div v-if="consultorio != null" class="botonesDeAbajo">
      <v-btn
        v-if="filtrarPorNombre.length > 0"
        class="mt-2"
        @click="dialog2 = true"
      >
        Revertir Cambios
      </v-btn>
      <v-spacer></v-spacer>

      <v-btn
        v-if="filtrarPorNombre.length > 0"
        color="primary"
        class="mt-2"
        @click="botonGuardar()"
      >
        Guardar Cambios
      </v-btn>
    </div>

    <v-dialog v-model="dialog" max-width="400">
      <v-card>
        <v-card-title class="headline">
          El inventario se va a actualizar,
          <br />
          ¿Deseas continuar?
        </v-card-title>

        <v-card-text>
          Los cambios ya no se podran revertir.
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn color="primary" text @click="dialog = false">
            Cancelar
          </v-btn>

          <v-btn color="primary" text @click="actualizarProductos()">
            Aceptar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialog2" max-width="400">
      <v-card>
        <v-card-title class="headline">
          Se van a revertir los cambios
          <br />
          ¿Deseas continuar?
        </v-card-title>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn color="primary" text @click="dialog2 = false">
            Cancelar
          </v-btn>

          <v-btn color="primary" text @click="revertirCambios()">
            Aceptar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialog3" max-width="400">
      <v-card>
        <v-card-title class="headline">
          Dejaste un campo vacio, <br />
          no puedes continuar.
        </v-card-title>

        <v-card-actions>
          <v-btn color="green darken-1" text @click="dialog3 = false">
            Aceptar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import axios from "axios";
import { mapState } from "vuex";
import { environment } from "../env/environment";
export default {
  data() {
    return {
      campoVacio: false,
      cantidad: 0,
      nuevaCantidad: 0,
      url: environment.url + "Producto",
      urlInventario: environment.url + "Inventario",
      productos: [],
      productosI: [],
      buscar: "",
      consultorios: [],
      consultorio: null,
      pagina: 1,
      paginas: 1,

      productosInventario: {
        id: "",
        nombre: "",
        descripcion: "",
        precio: 0,
        cantidad: 0,
        cantidadNueva: 0,
        consultorio: ""
      },

      inventario: {
        ProductosInventario: [],
        Consultorio: "",
        Usuario: {}
      },

      productosInventarioDetalle: {
        Id: "",
        Nombre: "",
        CantidadAnterior: 0,
        CantidadActualizada: 0,
        precio: 0,
      },

      dialog: false,
      dialog2: false,
      dialog3: false,
      auxiliarPUT: 0
    };
  },
  created() {
    this.initialize();
  },
  computed: {
    ...mapState(["user"]),
    filtrarPorNombre() {
      return this.productosI.filter(pr => {
        return pr.nombre.toLowerCase().includes(this.buscar.toLowerCase());
      });
    }
  },
  methods: {
    getConsultorios() {
      this.$vloading.show();
      axios
        .get(environment.url + "Consultorio")
        .then(response => {
          this.consultorios = response.data;

          if (this.consultorios.length == 0) {
            this.$toast.info("No hay registros consultorios");
          }
          this.$vloading.hide();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener los consultorios");
        });
    },
    getProductos() {
      this.productosI = [];
      this.productos = [];
      this.$vloading.show();
      axios
        .get(this.url + "/Consultorio/" + this.consultorio)

        .then(response => {
          this.productos = response.data;

          this.productos.forEach(pr => {
            this.productosI.push(
              (this.productosInventario = {
                id: pr.id,
                nombre: pr.nombre,
                descripcion: pr.descripcion,
                precio: pr.precio,
                cantidad: pr.cantidad,
                cantidadNueva: pr.cantidad,
                consultorio: pr.consultorio
              })
            );
          });

          this.$vloading.hide();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener productos");
        });
    },
    actualizarProducto(producto) {
      let aux = this.auxiliarPUT;
      let tam = this.productosI.length;

      axios
        .put(this.url, producto)
        .then(() => {
          this.$vloading.hide();
          // this.$toast.success("Se actualizo el inventario correctamente");

          if (aux === tam) {
            this.getProductos();
          }
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudo actualizar el inventario");
        });
    },

    actualizarProductos() {
      this.dialog = true;

      let listaProductosDetalle = [];

      this.productosI.forEach(pr => {
        let producto = {
          id: pr.id,
          nombre: pr.nombre,
          descripcion: pr.descripcion,
          precio: parseFloat(pr.precio),
          cantidad: parseInt(pr.cantidadNueva),
          consultorio: pr.consultorio
        };

        let productosDetalle = {
          id: pr.id,
          nombre: pr.nombre,
          cantidadAnterior: parseInt(pr.cantidad),
          CantidadActualizada: parseInt(pr.cantidadNueva),
          precio: parseInt(pr.precio),
        };

        if (
          productosDetalle.CantidadActualizada !=
          productosDetalle.cantidadAnterior
        ) {
          listaProductosDetalle.push(productosDetalle);
        }

        this.auxiliarPUT++;
        this.actualizarProducto(producto);
      });
      let InventarioFinal = {
        ProductosInventario: listaProductosDetalle,
        Consultorio: this.consultorio,
        Usuario: this.user
      };
      this.inventario = InventarioFinal;
      let listaPDTam = InventarioFinal.ProductosInventario.length;
      if (listaPDTam > 0) {
        this.registrarInventario(this.inventario);
      }

      this.dialog = false;
      this.buscar = "";
      this.auxiliarPUT = 0;
    },

    registrarInventario(inventario) {
      this.$vloading.show();
      axios
        .post(this.urlInventario, inventario)
        .then(() => {
          this.$vloading.hide();
          this.$toast.success("Se actualizo el inventario correctamente");
        })
        .catch(() => {
          this.$vloading.hide();
        });
    },
    revertirCambios() {
      this.productosI.forEach(pr => {
        pr.cantidadNueva = pr.cantidad;
      });

      this.dialog2 = false;
    },

    botonGuardar() {
      this.filtrarPorNombre.forEach(pr => {
        if (pr.cantidadNueva === "") {
          this.dialog3 = true;
          return;
        }
      });

      if (this.dialog3 == false) {
        this.dialog = true;
      }
    },

    initialize() {
      if (this.user.role == "ADMIN") {
        this.getConsultorios();
      } else if (this.user.role == "Dentista") {
        this.consultorio = this.user.consultorio;
        this.getProductos();
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.contadores {
  display: flex;
  margin-top: 20px;
}

.campoNumero {
  max-width: 80px;
  text-align: center;
}

.tablaInventario {
  max-height: 450px;
  overflow-x: auto;
}

.botonesDeAbajo {
  display: flex;
}

.sinConsultorio {
  text-align: center;
  border-bottom: thin solid rgba(0, 0, 0, 0.12);
  border-right: thin solid rgba(0, 0, 0, 0.12);
  border-left: thin solid rgba(0, 0, 0, 0.12);
}

.bordeTabla {
  border-bottom: thin solid rgba(0, 0, 0, 0.12);
  border-right: thin solid rgba(0, 0, 0, 0.12);
  border-left: thin solid rgba(0, 0, 0, 0.12);
}
</style>
