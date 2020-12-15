using DentiSmart.Domain.Contracts;
using DentiSmart.Domain.Models;
using DentiSmart.Infrastructure.DataBase;
using Microsoft.Extensions.Hosting;
using Newtonsoft.Json.Linq;
using RestSharp;
using System;
using System.Collections.Generic;
using System.Security.Policy;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace DentiSmart.API.Services
{
    public class NotificacionWhatsappHostedService : IHostedService, IDisposable
    {
        private Timer timer;
        private readonly string URL_WHATSAPP_API = "http://dentismart.ga:3001/whatsapp/sendmessage";
        private readonly ICitaRepository _citaRepository;
        public  NotificacionWhatsappHostedService(ICitaRepository citaRepository)
        {
            _citaRepository = citaRepository;
        }
        public void Dispose()
        {
            timer?.Dispose();
        }

        public Task StartAsync(CancellationToken cancellationToken)
        {
            timer = new Timer(EnviarNotificaciones, null, TimeSpan.Zero, TimeSpan.FromDays(1));
            return Task.CompletedTask;
        }

        public Task StopAsync(CancellationToken cancellationToken)
        {
            timer?.Change(Timeout.Infinite, 0);
            return Task.CompletedTask;
        }
        private void EnviarNotificaciones(Object state) {

            List<Cita> citasDiaSiguiente = _citaRepository.GetCitasDiaSiguiente();
            RestClient client = new RestClient(URL_WHATSAPP_API);
            RestRequest request;
            foreach (Cita cita in citasDiaSiguiente)
            {
                JObject paciente = new JObject();
                request = new RestRequest(Method.POST);
                paciente.Add("phone", "521"+cita.Paciente.Telefono);
                paciente.Add("body", $"Hola {cita.Paciente.Nombre} 🌚 este es un recordatorio de qué tienes una cita a las {string.Format("{0:hh:mm tt}",cita.FechaCita)} del día de mañana 🦷😷🩺 no faltes!!!!");
                request.AddParameter("application/json", paciente, ParameterType.RequestBody);
                client.Execute(request);
                JObject dentista = new JObject();
                request = new RestRequest(Method.POST);
                dentista.Add("phone", "521" + cita.Dentista.Telefono);
                dentista.Add("body", $"Hola Dentista 🌚 este es un recordatorio de qué tienes una cita a las {string.Format("{0:hh:mm tt}", cita.FechaCita)} del día de mañana 🦷😷🩺 no faltes!!!!");
                request.AddParameter("application/json", dentista, ParameterType.RequestBody);
                client.Execute(request);
            }
      
        }
        
    }
}
