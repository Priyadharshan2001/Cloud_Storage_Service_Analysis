import AmazonImage from '@/assets/Aleena.jpeg';
import MicrosoftImage from '@/assets/Simran.jpeg';
import DropBoxImage from '@/assets/Vamsi.jpeg';
import ICloudImage from '@/assets/Hari.jpeg';
import GoogleImage from '@/assets/Priyadarshan.jpeg';

const AboutUs = () => {
  const teamMembers = [
    {
      name: 'Priyadharshan',
      image: GoogleImage,
      role: 'Google'
    },
    {
      name: 'Aleena',
      image: AmazonImage,
      role: 'Amazon'
    },
    {
      name: 'Vamsi',
      image: DropBoxImage,
      role: 'DropBox'
    },
    {
      name: 'Harinath',
      image: ICloudImage,
      role: 'ICloud'
    },
    {
      name: 'Simran',
      image: MicrosoftImage,
      role: 'Microsoft'
    }
  ];

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-8">
      <h1 className="text-3xl font-bold text-gray-800 mb-12">Our Team</h1>
      
      <div className="flex justify-center items-center w-full max-w-screen-xl mx-auto">
        <div className="flex flex-nowrap gap-8 justify-center items-center overflow-x-auto py-4 px-2 w-full">
          {teamMembers.map((member, index) => (
            <div 
              key={index}
              className="flex-shrink-0 flex flex-col items-center gap-4 w-48"
            >
              <div className="w-32 h-32 rounded-full overflow-hidden border-4 border-white shadow-lg">
                <img 
                  src={member.image} 
                  alt={member.name}
                  className="w-full h-full object-cover"
                />
              </div>
              <div className="text-center">
                <h2 className="text-xl font-semibold text-gray-800">{member.name}</h2>
                <p className="text-gray-600">{member.role}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default AboutUs;