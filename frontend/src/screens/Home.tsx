import AmazonImage from "@/assets/amazon.jpg";
import MicrosoftImage from "@/assets/Microsoft.jpg";
import DropBoxImage from "@/assets/Dropbox.jpg";
import ICloudImage from "@/assets/ICloud.jpg";
import GoogleImage from "@/assets/google.png";

const images = [
  { 
    image: AmazonImage, 
    name: "AMAZON S3",
    url: "https://aws.amazon.com/s3/"
  },
  { 
    image: MicrosoftImage, 
    name: "MICROSOFT AZURE",
    url: "https://www.microsoft.com/en-ca/microsoft-365/buy/compare-all-microsoft-365-products"
  },
  { 
    image: DropBoxImage, 
    name: "DROPBOX",
    url: "https://www.dropbox.com/plans?billing=yearly"
  },
  { 
    image: GoogleImage, 
    name: "GOOGLE ONE",
    url: "https://one.google.com/about/plans"
  },
  { 
    image: ICloudImage, 
    name: "ICLOUD",
    url: "https://www.apple.com/ca/icloud/#compare-plans"
  },
];

const Home = () => {
  return (
    <div className="flex flex-col justify-center items-center h-screen bg-gray-100">
      <h1 className="text-3xl font-bold text-center text-gray-800 mb-20">
        Nimbus Navigators - Cloud Storage Service Analysis
      </h1>

      <div className="relative w-96 h-96 flex justify-center items-center mb-12">
        {/* Rotating container */}
        <div 
          className="absolute w-full h-full"
          style={{ 
            animation: "rotate 20s linear infinite",
            transformOrigin: "center center"
          }}
        >
          {images.map((service, index) => {
            const angle = (360 / images.length) * index;
            const radius = 150;
            const x = radius * Math.cos((angle * Math.PI) / 180);
            const y = radius * Math.sin((angle * Math.PI) / 180);

            return (
              <a
                href={service.url}
                key={index}
                target="_blank"
                rel="noopener noreferrer"
                className="absolute w-24 h-24 rounded-full border-4 border-sky-500 shadow-lg hover:border-blue-600 hover:scale-110 transition-all duration-300"
                style={{
                  top: `calc(50% - 3rem + ${y}px)`,
                  left: `calc(50% - 3rem + ${x}px)`,
                  transform: "translate(-50%, -50%)",
                  animation: `pop 0.8s ease-out ${index * 0.2}s forwards`,
                }}
              >
                <img
                  className="w-full h-full rounded-full object-cover"
                  src={service.image}
                  alt={`${service.name} logo`}
                />
              </a>
            );
          })}
        </div>
      </div>

      {/* Global styles in a proper style tag */}
      <style>{`
        @keyframes pop {
          0% { transform: translate(-50%, -50%) scale(0); opacity: 0; }
          60% { transform: translate(-50%, -50%) scale(1.1); opacity: 1; }
          100% { transform: translate(-50%, -50%) scale(1); opacity: 1; }
        }
        @keyframes rotate {
          from { transform: rotate(0deg); }
          to { transform: rotate(360deg); }
        }
      `}</style>
    </div>
  );
};

export default Home;