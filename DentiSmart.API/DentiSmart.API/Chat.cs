using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.SignalR;
using DentiSmart.Domain.Models;


namespace DentiSmart.API
{

    public class Chat : Hub
    {

        static List<DetalleConexion> ConnectedUsers = new List<DetalleConexion>();
        static List<Mensajes> CurrentMessage = new List<Mensajes>();



        public override Task OnConnectedAsync()
        {
            Console.WriteLine("ChatHub hub connected");
            return base.OnConnectedAsync();
        }

        public override Task OnDisconnectedAsync(Exception exception)
        {
            Console.WriteLine("ChatHub hub disconnected");
            return base.OnDisconnectedAsync(exception);
        }

        public async Task SendMessage(string emisor, string receptor, string message)
        {
            Console.WriteLine("Message received");
            await Clients.All.SendAsync("ReceiveMessage", emisor, receptor, message);


        }

        public async Task SendMessagetoPrivateUser(string user, string message)
        {

        

            await Clients.User(user).SendAsync("ReceiveMessage", message);


        }



        public void Connect(string UserName, string UserID)
        {
            var id = Context.ConnectionId;

            if (ConnectedUsers.Count(x => x.ConnectionId == id) == 0)
            {
                ConnectedUsers.Add(new DetalleConexion { ConnectionId = id, NombreUsuario = UserName + "-" + UserID, UsuarioID = UserID });
            }
            DetalleConexion CurrentUser = ConnectedUsers.Where(u => u.ConnectionId == id).FirstOrDefault();
             }

     

}
}
