<template>
  <v-container>
    <h3 v-if="notiNuevoMensaje != 0" class="notificacionChat">
      {{ notiNuevoMensaje }}
    </h3>
    <v-btn
      class="botonChat"
      color="primary"
      dark
      x-large
      fab
      @click="abrirChat()"
    >
      <v-icon>mdi-message</v-icon>
    </v-btn>

    <v-row
      v-if="chatAbierto"
      class="cardChat no-gutters elevation-4"
      :style="[
        $vuetify.theme.dark
          ? { background: '#1E1E1E' }
          : { background: 'white' }
      ]"
    >
      <v-col cols="12">
        <v-toolbar color="primary" dark dense flat style="height: 100%">
          <v-toolbar-title>
            Chat recientes
          </v-toolbar-title>
        </v-toolbar>
      </v-col>
      <v-col
        cols="3"
        sm="3"
        class="flex-grow-1 flex-shrink-0"
        style="border-right: 1px solid #0000001f;"
      >
        <v-responsive class="tamañoMedia overflow-y-auto fill-height">
          <v-list subheader>
            <v-list-item-group v-model="activeChat">
              <template v-for="(item, index) in usuarios">
                <v-list-item
                  :key="`parent${index}`"
                  :value="item.id"
                  @click="mostrarChat(item)"
                >
                  <v-tooltip bottom>
                    <template v-slot:activator="{ on, attrs }">
                      <v-list-item-avatar
                        v-bind="attrs"
                        v-on="on"
                        color="grey lighten-1 white--text"
                      >
                        <v-icon v-if="item.id == 1">
                          mdi-account-plus
                        </v-icon>
                        <v-avatar size="45">
                          <span
                            v-if="!item.foto && item.id != 1"
                            class="white--text headline"
                            >{{ item.iniciales }}</span
                          >
                          <v-img v-if="item.foto" :src="item.foto" />
                        </v-avatar>
                      </v-list-item-avatar>
                    </template>
                    <span>{{ item.title }}</span>
                  </v-tooltip>
                </v-list-item>
                <v-divider :key="`chatDivider${index}`" class="my-0" />
              </template>
            </v-list-item-group>
          </v-list>
        </v-responsive>
      </v-col>
      <v-col cols="9" class="flex-grow-1 flex-shrink-0">
        <v-responsive class="tamañoMedia overflow-y-hidden ">
          <v-card flat class="d-flex flex-column fill-height" id="app">
            <v-card-title v-if="banderaMas" style="width: 400px;">
              {{ UserNameActivo }}
            </v-card-title>
            <v-divider></v-divider>
            <v-card-text
              v-if="banderaMas"
              id="chatScroll"
              class="flex-grow-1 overflow-y-auto"
            >
              <template v-for="(msg, i) in messages">
                <div :key="i" v-if="msg.content.includes('data:image')">
                  <div :key="i" v-if="idActivo == msg.idOtroUsuario">
                    <div
                      :key="i"
                      :class="{ 'd-flex flex-row-reverse': msg.me }"
                    >
                      <v-menu offset-y>
                        <template v-slot:activator="{ on }">
                          <v-hover
                            style="cursor: pointer"
                            v-slot:default="{ hover }"
                          >
                            <v-chip
                              :color="msg.me ? 'primary' : ''"
                              dark
                              style="height:auto;white-space: normal;"
                              class="pa-4 mb-2"
                              v-on="on"
                            >
                              <v-img :src="msg.content"></v-img>

                              <v-icon v-if="hover && msg.me" small>
                                mdi-dots-horizontal
                              </v-icon>
                            </v-chip>
                          </v-hover>
                        </template>
                        <v-list v-if="msg.me">
                          <v-list-item>
                            <v-list-item-title @click="borrarMensaje(msg)"
                              >Borrar</v-list-item-title
                            >
                          </v-list-item>
                        </v-list>
                      </v-menu>
                    </div>
                  </div>
                </div>

                <div :key="i" v-if="!msg.content.includes('data:image')">
                  <div :key="i" v-if="idActivo == msg.idOtroUsuario">
                    <div
                      :key="i"
                      :class="{ 'd-flex flex-row-reverse': msg.me }"
                    >
                      <v-menu offset-y>
                        <template v-slot:activator="{ on }">
                          <v-hover v-slot:default="{ hover }">
                            <v-chip
                              :color="msg.me ? 'primary' : ''"
                              dark
                              style="height:auto;white-space: normal;"
                              class="pa-4 mb-2"
                              v-on="on"
                            >
                              {{ msg.content }}
                              <sub class="ml-2" style="font-size: 0.7rem;">{{
                                msg.created_at
                              }}</sub>
                              <v-icon v-if="hover && msg.me" small>
                                mdi-dots-horizontal
                              </v-icon>
                            </v-chip>
                          </v-hover>
                        </template>
                        <v-list v-if="msg.me">
                          <v-list-item>
                            <v-list-item-title @click="borrarMensaje(msg)"
                              >Borrar</v-list-item-title
                            >
                          </v-list-item>
                        </v-list>
                      </v-menu>
                    </div>
                  </div>
                </div>
              </template>
              <span id="final"></span>
            </v-card-text>
            <v-divider></v-divider>

            <v-card-text v-if="banderaMas" class="contenedor flex-shrink-1">
              <v-text-field
                v-model="messageForm.content"
                label="Escribe un
              mensaje"
                type="text"
                clearable
                @click:clear="borrarClearable()"
                no-details
                outlined
                :readonly="banderaImagen"
                @keyup.enter="SignalR()"
                hide-details
              />

              <v-btn
                color="warning"
                style="position: relative; right: 20px;"
                dark
                fab
                @click="SignalR()"
              >
                <v-icon>mdi-send</v-icon>
              </v-btn>
              <v-btn color="blue" class="clip" fab dark>
                <v-file-input
                  hide-input
                  @change="mandarImagen()"
                  accept="image/png, image/jpeg"
                  v-model="imagen"
                  style="display:contents"
                ></v-file-input>
              </v-btn>
              <v-btn id="ayuda" @click="ayuda()" style="display: none;"
                >ayuda</v-btn
              >
            </v-card-text>
          </v-card>
        </v-responsive>
      </v-col>
    </v-row>
    <template>
      <v-row justify="center">
        <v-dialog v-model="dialog" scrollable max-width="300px">
          <v-card>
            <v-card-title
              >Seleccione
              {{ this.user.role == "Paciente" ? "dentista" : "paciente" }}
            </v-card-title>
            <v-divider></v-divider>
            <v-card-text style="height: 300px;">
              <v-radio-group v-model="usuarioSeleccionado" column>
                <v-radio
                  v-for="(item, index) in datosUsuarios"
                  :key="index"
                  :label="
                    `${item.nombre} ` +
                      `${item.apellidoPat} ` +
                      `${item.apellidoMat} `
                  "
                  :value="item"
                ></v-radio>
              </v-radio-group>
            </v-card-text>
            <v-divider></v-divider>
            <v-card-actions>
              <v-btn color="blue darken-1" text @click="dialog = false">
                Cerrar
              </v-btn>
              <v-btn color="blue darken-1" text @click="agregarUsuario()">
                Seleccionar
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-row>
    </template>
  </v-container>
</template>

<script>
import * as signalR from "@aspnet/signalr";
import axios from "axios";
import { environment } from "../../env/environment";
import { mapState } from "vuex";

export default {
  data: () => ({
    name: "chatComponent",
    chatAbierto: false,
    banderaImagen: false,
    activeChat: 1,
    idActivo: 1,
    imagen: null,
    urlimg: null,
    totalMensajes: [],
    nombreUsuarioActivo: "",
    datosUsuarios: [],
    banderaMas: false,
    listaUsuarios: [],
    auxMensajes: [],
    base64: "",
    dialog: false,
    url: environment.url + "usuario",
    urlMensajes: environment.url + "Mensajes",
    urlMensajesUsuarios: environment.url + "Mensajes/",
    auxMensajesxUsuario: [],
    usuarioSeleccionado: "",
    notiNuevoMensaje: 0,
    connection: new signalR.HubConnectionBuilder()
      .withUrl(process.env.VUE_APP_API_NETCORE_DENTISMART_SIGNLAR)
      .build(),
    usuarios: [
      {
        id: 1,
        title: "+",
        active: true,
        foto: null,
        nombreUsuario: "",
        iniciales: ""
      }
    ],
    UserNameActivo: "",
    messages: [],
    messageForm: {
      content: "",
      me: true,
      idOtroUsuario: 0,
      created_at: "11:11am"
    }
  }),
  methods: {
    borrarClearable() {
      console.log("hola");
      this.banderaImagen = false;
      this.urlimg = null;
      this.imagen = null;
    },
    abrirChat() {
      if (this.chatAbierto) {
        this.chatAbierto = false;
        this.listaUsuarios = [];
        this.usuarios = [
          {
            id: 1,
            title: "+",
            active: true,
            foto: null,
            nombreUsuario: "",
            iniciales: ""
          }
        ];
      } else {
        this.chatAbierto = true;
        this.obtenerMensajes(this.user.nombreUsuario);
        this.notiNuevoMensaje = 0;
      }
    },
    borrarMensaje(mensaje) {
      var mensajeBorrado = this.urlMensajesUsuarios + mensaje.idMensaje;
      axios
        .delete(mensajeBorrado)
        .then(() => {
          for (var i = 0; i < this.messages.length; i++) {
            if (this.messages[i].idMensaje == mensaje.idMensaje)
              this.messages.splice(i, 1);
          }
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudo eliminar el mensaje ");
        });
    },
    agregarUsuario() {
      this.dialog = false;

      const auxUsuario = {
        id: this.usuarioSeleccionado.id,
        title:
          this.usuarioSeleccionado.nombre +
          " " +
          this.usuarioSeleccionado.apellidoPat +
          " " +
          this.usuarioSeleccionado.apellidoMat,
        active: false,
        foto: this.usuarioSeleccionado.foto,
        nombreUsuario: this.usuarioSeleccionado.nombreUsuario,
        iniciales:
          this.usuarioSeleccionado.nombre.charAt(0) +
          this.usuarioSeleccionado.apellidoPat.charAt(0)
      };

      this.usuarios.push(auxUsuario);

      var aux = {
        idUsuarioReceptor: this.usuarioSeleccionado.id,
        userNameUsuarioReceptor: this.usuarioSeleccionado.nombreUsuario,
        idUsuarioTransmisor: this.user.id,
        userNameUsuarioTransmisor: this.user.nombreUsuario
      };

      this.listaUsuarios.push(aux);
    },
    mostrarChat(item) {
      var aux = [];

      this.usuarios.forEach(usu => {
        if (usu.id == item.id) {
          if (!usu.active) {
            usu.active = true;
            aux.push(usu);
          } else {
            usu.active = false;
            aux.push(usu);
          }
        } else {
          usu.active = false;
          aux.push(usu);
        }
      });

      this.usuarios = aux;

      console.log(item.active);
      if (item.title == "+") {
        this.obtenerUsuarios();
        this.banderaMas = false;
        this.dialog = true;
        return;
      } else if (item.active) this.banderaMas = true;
      else this.banderaMas = false;

      this.notiNuevoMensaje = 0;
      this.messages = [];

      var urlObtMensajes =
        this.urlMensajesUsuarios +
        this.user.nombreUsuario +
        "?username=" +
        this.user.nombreUsuario;

      axios
        .get(urlObtMensajes)
        .then(res => {
          res.data.forEach(msg => {
            if (
              this.idActivo == msg.idUsuarioReceptor ||
              this.idActivo == msg.idUsuarioTransmisor
            ) {
              if (msg.idUsuarioTransmisor == this.user.id) {
                var objeto = {
                  content: msg.mensaje,
                  me: true,
                  idOtroUsuario: msg.idUsuarioReceptor,
                  created_at: msg.fecha,
                  idMensaje: msg.id
                };
                this.messages.push(objeto);
                console.log("Push 1");
              } else {
                var objeto2 = {
                  content: msg.mensaje,
                  me: false,
                  idOtroUsuario: msg.idUsuarioTransmisor,
                  created_at: msg.fecha,
                  idMensaje: msg.id
                };
                this.messages.push(objeto2);
                console.log("Push 2");
              }
            }
          });

          this.$vloading.hide();
        })
        .catch(() => {
          this.$vloading.hide();
        });

      this.idActivo = item.id;
      this.nombreUsuarioActivo = item.nombreUsuario;
      this.usuarios.forEach(pr => {
        if (pr.id == item.id) {
          this.UserNameActivo = pr.title.toUpperCase();
          pr.active == true;
        } else pr.active == false;
      });
    },

    obtenerUsuarios() {
      this.$vloading.show();
      this.datosUsuarios = [];
      let urlUser = this.url + "/role/Paciente";
      if (this.user.role == "Paciente") urlUser = this.url + "/role/Dentista";
      axios
        .get(urlUser)
        .then(res => {
          this.$vloading.hide();
          res.data.forEach(usu => {
            if (usu.id != this.user.id) {
              if (
                this.listaUsuarios.some(
                  item =>
                    item.userNameUsuarioReceptor == usu.nombreUsuario ||
                    item.userNameUsuarioTransmisor == usu.nombreUsuario
                )
              )
                return;
              else this.datosUsuarios.push(usu);
            }
          });
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener los Usuarios");
        });
    },

    SignalR() {
      console.log("entro una vez");
      if (this.urlimg != undefined) {
        this.messageForm.content = this.urlimg;
        this.urlimg = null;
        this.imagen = null;
      }

      if (this.connection.state === signalR.HubConnectionState.Connected) {
        this.connection.invoke(
          "SendMessage",
          this.user.nombreUsuario,
          this.nombreUsuarioActivo,
          this.messageForm.content
        );
      } else {
        this.connection
          .start()
          .then(() =>
            this.connection.invoke(
              "SendMessage",
              this.user.nombreUsuario,
              this.nombreUsuarioActivo,
              this.messageForm.content
            )
          );
      }
      this.messageForm.content = "";
      this.urlimg = null;
    },
    ayuda() {
      var objDiv = document.getElementById("chatScroll");
      objDiv.scrollTop = objDiv.scrollHeight;
    },
    mandarImagen() {
      this.base64 = this.urlimg;
      this.messageForm.content = this.imagen.name;
      this.banderaImagen = true;

      if (this.imagen !== undefined) {
        var that = this; //the important bit

        var reader = new FileReader();
        reader.readAsDataURL(this.imagen);
        reader.onloadend = function() {
          that.urlimg = reader.result;
        };
      }
    },
    obtenerMensajes(userName) {
      var urlObtMensajes =
        this.urlMensajesUsuarios + userName + "?username=" + userName;

      this.$vloading.show();

      axios
        .get(urlObtMensajes)
        .then(res => {
          res.data.forEach(res => {
            var aux = {
              idUsuarioReceptor: res.idUsuarioReceptor,
              userNameUsuarioReceptor: res.userNameUsuarioReceptor,
              idUsuarioTransmisor: res.idUsuarioTransmisor,
              userNameUsuarioTransmisor: res.userNameUsuarioTransmisor
            };

            var auxListaUsuarios;
            if (res.userNameUsuarioTransmisor == this.user.nombreUsuario)
              auxListaUsuarios = res.userNameUsuarioReceptor;
            else auxListaUsuarios = res.userNameUsuarioTransmisor;

            var res2 = [];
            if (this.listaUsuarios.length != 0) {
              this.listaUsuarios.forEach(res => {
                res2 = res2 + res;
                if (
                  this.listaUsuarios.some(
                    item =>
                      item.userNameUsuarioTransmisor === auxListaUsuarios ||
                      item.userNameUsuarioReceptor === auxListaUsuarios
                  )
                ) {
                  res2 = res;
                } else this.listaUsuarios.push(aux);
              });
            } else {
              this.listaUsuarios.push(aux);
            }
          });

          this.listaUsuarios.forEach(usuario => {
            var usuarioAux = "";
            if (usuario.userNameUsuarioReceptor !== this.user.nombreUsuario)
              usuarioAux = this.url + "/" + usuario.idUsuarioReceptor;
            else usuarioAux = this.url + "/" + usuario.idUsuarioTransmisor;

            axios
              .get(usuarioAux)
              .then(res => {
                let usuariosConectados = {
                  id: res.data.id,
                  title:
                    res.data.nombre +
                    " " +
                    res.data.apellidoPat +
                    " " +
                    res.data.apellidoMat,
                  active: false,
                  foto: res.data.foto,
                  nombreUsuario: res.data.nombreUsuario,
                  iniciales:
                    res.data.nombre.charAt(0) + res.data.apellidoPat.charAt(0)
                };

                this.usuarios.push(usuariosConectados);
              })
              .catch(() => {
                this.$vloading.hide();
              });
          });
          this.$vloading.hide();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("Error: No se pudo obtener los mensajes");
        });
    },

    añadirMensaje(mensaje) {
      this.banderaImagen = false;
      if (mensaje.UserNameUsuarioTransmisor == this.user.nombreUsuario) {
        axios
          .post(this.urlMensajes, {
            Mensaje: mensaje.Mensaje,
            IDUsuarioTransmisor: mensaje.IDUsuarioTransmisor,
            IDUsuarioReceptor: mensaje.IDUsuarioReceptor,
            UserNameUsuarioTransmisor: mensaje.UserNameUsuarioTransmisor,
            UserNameUsuarioReceptor: mensaje.UserNameUsuarioReceptor,
            Fecha: mensaje.Fecha
          })
          .then(() => {
            if (this.urlimg != undefined) {
              this.urlimg = null;
              this.imagen = null;
            }

            if (mensaje.IDUsuarioTransmisor == this.user.id) {
              var objeto3 = {
                content: mensaje.Mensaje,
                me: true,
                idOtroUsuario: mensaje.IDUsuarioReceptor,
                created_at: mensaje.Fecha,
                idMensaje: ""
              };
              this.messages.push(objeto3);
              console.log("Push 3");
              this.ayuda();
            } else {
              var objeto4 = {
                content: mensaje.Mensaje,
                me: false,
                idOtroUsuario: mensaje.IDUsuarioTransmisor,
                created_at: mensaje.Fecha,
                idMensaje: mensaje.id
              };
              this.messages.push(objeto4);
              console.log("Push 4");
              this.ayuda();
            }
          })
          .catch(() => {});
      } else {
        if (mensaje.IDUsuarioTransmisor == this.user.id) {
          var objeto3 = {
            content: mensaje.Mensaje,
            me: true,
            idOtroUsuario: mensaje.IDUsuarioReceptor,
            created_at: mensaje.Fecha,
            idMensaje: mensaje.id
          };
          this.messages.push(objeto3);
          console.log("Push 5");
          this.ayuda();
        } else if (mensaje.IDUsuarioReceptor == this.idActivo) {
          var objeto4 = {
            content: mensaje.Mensaje,
            me: false,
            idOtroUsuario: mensaje.IDUsuarioTransmisor,
            created_at: mensaje.Fecha,
            idMensaje: mensaje.id
          };
          this.messages.push(objeto4);
          console.log("Push 6");
        }
      }
    }
  },

  created() {
    this.connection.start();
    this.UserNameActivo = this.usuarios[0].title.toUpperCase();

    this.connection.on("ReceiveMessage", (emisor, receptor, data) => {
      console.log("Emisor " + emisor);
      console.log("Receptor: " + receptor);
      console.log("Mensaje: " + data);

      if (
        this.user.nombreUsuario == emisor ||
        this.user.nombreUsuario == receptor
      ) {
        var fecha = new Date();
        var auxHora = fecha.getHours();
        var auxMinutos = fecha.getMinutes();

        if (auxMinutos > 0 && auxMinutos < 10) auxMinutos = "0" + auxMinutos;

        var hora = fecha.getHours() + ":" + auxMinutos;
        if (auxHora > 12) hora = hora + " P.M.";
        else hora = hora + " A.M.";

        if (emisor == this.user.nombreUsuario) {
          console.log("socket 1");
          const insertdata = {
            Mensaje: data,
            IDUsuarioTransmisor: this.user.id,
            UserNameUsuarioTransmisor: this.user.nombreUsuario,
            IDUsuarioReceptor: this.idActivo,
            UserNameUsuarioReceptor: this.nombreUsuarioActivo,
            Fecha: hora,
            me: true,
            created_at: hora
          };
          this.añadirMensaje(insertdata);
        } else {
          console.log("socket 2");

          const insertdata2 = {
            Mensaje: data,
            IDUsuarioTransmisor: this.idActivo,
            UserNameUsuarioTransmisor: emisor,
            IDUsuarioReceptor: this.user.id,
            UserNameUsuarioReceptor: this.user.nombreUsuario,
            Fecha: hora,
            me: false,
            created_at: hora
          };

          this.añadirMensaje(insertdata2);
          this.notiNuevoMensaje++;
        }
      }
    });
  },

  computed: {
    ...mapState(["user"]),
    boton() {
      switch (this.$vuetify.breakpoint.name) {
        case "xs":
          return 10;
        case "sm":
          return 20;
        case "md":
          return 30;
        case "lg":
          return 40;
        case "xl":
          return 50;
        default:
          return 20;
      }
    },
    iniciales() {
      return this.usuarioSeleccionado.nombre ||
        this.usuarioSeleccionado.apellidoPat
        ? this.usuarioSeleccionado.nombre.charAt(0).toUpperCase() +
            this.usuarioSeleccionado.apellidoPat.charAt(0).toUpperCase()
        : "";
    }
  },

  watch: {
    top(val) {
      this.bottom = !val;
    },
    right(val) {
      this.left = !val;
    },
    bottom(val) {
      this.top = !val;
    },
    left(val) {
      this.right = !val;
    }
  }
};
</script>

<style lang="scss">
.botonChat {
  position: absolute;
  bottom: 70px;
  right: 30px;
  z-index: 1;
}

.textarea {
  padding-top: 30px;
  padding-bottom: 30px;
  padding-left: 10px;
  padding-right: 10px;
}

.notificacionChat {
  background-color: red;
  padding: 2px;
  color: white;
  border-radius: 20px;
  position: absolute;
  bottom: 130px;
  right: 40px;
  z-index: 30;
}

.botonRedondo {
  width: 30px;
  height: 30px;
  background-color: #03800b;
  margin: 5px;
  padding: 10px;
  -webkit-border-radius: 50px;
  -moz-border-radius: 50px;
  border-radius: 50px;
  font-size: 11px;
  line-height: 32px;
  text-transform: uppercase;
  float: left;
}

.contenedor {
  display: flex;
}

@media screen and (min-width: 800px) {
  .cardChat {
    position: absolute;
    bottom: 160px;
    right: 23px;
    z-index: 100;
    width: 35%;
    height: 80%;
  }

  .cardChat:before {
    content: "";
    position: absolute;
    top: 100%; // half way down (vertical center).
    left: 90%;
    border-left: 10px solid transparent;
    border-right: 10px solid transparent;
    border-top: 15px solid gray;
    z-index: 1;
  }

  .tamañoMedia {
    height: 60vh;
  }
}

@media screen and (max-width: 799px) {
  .cardChat {
    position: absolute;
    bottom: 160px;
    right: 23px;
    z-index: 100;
    width: 80%;
  }

  .cardChat:before {
    content: "";
    position: absolute;
    top: 100%; // half way down (vertical center).
    left: 85%;
    border-left: 10px solid transparent;
    border-right: 10px solid transparent;
    border-top: 15px solid gray;
    z-index: 1;
  }

  .tamañoMedia {
    height: 430px;
  }

  .fondoblanco {
    background-color: white;
  }
}
</style>
