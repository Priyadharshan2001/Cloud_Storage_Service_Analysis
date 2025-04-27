import { useState } from 'react';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';

const Subscribe = () => {
  const [formData, setFormData] = useState({
    fullName: '',
    dob: '',
    phone: '+1',
    email: '',
    password: ''
  });

  const [errors, setErrors] = useState({
    fullName: '',
    dob: '',
    phone: '',
    email: '',
    password: ''
  });

  const validateDOB = (dob: string) => {
    if (!/^\d{2}-\d{2}-\d{4}$/.test(dob)) {
      return 'Please use MM-DD-YYYY format';
    }

    const [monthStr, dayStr, yearStr] = dob.split('-');
    const month = parseInt(monthStr, 10);
    const day = parseInt(dayStr, 10);
    const year = parseInt(yearStr, 10);

    // Validate month (1-12)
    if (month < 1 || month > 12) {
      return 'Month must be between 01-12';
    }

    // Validate day based on month
    const daysInMonth = new Date(year, month, 0).getDate();
    if (day < 1 || day > daysInMonth) {
      return `Day must be between 01-${daysInMonth} for this month`;
    }

    // Validate year (not in future)
    const currentYear = new Date().getFullYear();
    if (year > currentYear) {
      return 'Year cannot be in the future';
    }

    return '';
  };

  const validateForm = () => {
    let isValid = true;
    const newErrors = {
      fullName: '',
      dob: '',
      phone: '',
      email: '',
      password: ''
    };

    // Full Name validation
    if (!formData.fullName.trim()) {
      newErrors.fullName = 'Full name is required';
      isValid = false;
    }

    // Date of Birth validation
    if (!formData.dob) {
      newErrors.dob = 'Date of birth is required';
      isValid = false;
    } else {
      const dobError = validateDOB(formData.dob);
      if (dobError) {
        newErrors.dob = dobError;
        isValid = false;
      }
    }

    // Phone Number validation (+1 followed by 10 digits)
    if (!formData.phone) {
      newErrors.phone = 'Phone number is required';
      isValid = false;
    } else if (!/^\+1\d{10}$/.test(formData.phone)) {
      newErrors.phone = 'Please enter +1 followed by 10 digits';
      isValid = false;
    }

    // Email validation
    if (!formData.email) {
      newErrors.email = 'Email is required';
      isValid = false;
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = 'Please enter a valid email';
      isValid = false;
    }

    // Password validation (min 8 chars, 1 uppercase, 1 special, 1 number)
    if (!formData.password) {
      newErrors.password = 'Password is required';
      isValid = false;
    } else if (!/^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+])[A-Za-z\d!@#$%^&*()_+]{8,}$/.test(formData.password)) {
      newErrors.password = 'Password must contain at least 8 characters, including 1 uppercase, 1 number, and 1 special character';
      isValid = false;
    }

    setErrors(newErrors);
    return isValid;
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (validateForm()) {
      alert(`Thank you for subscribing, ${formData.fullName}! We'll contact you at ${formData.email}`);
      console.log('Form data:', formData);
    }
  };

  const handlePhoneChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    let value = e.target.value;
    if (!value.startsWith('+1')) {
      value = '+1' + value.replace(/\D/g, '');
    }
    const digits = value.replace(/\D/g, '').slice(1);
    value = '+1' + digits.slice(0, 10);
    setFormData({...formData, phone: value});
  };

  const handleDOBChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    let value = e.target.value;
    // Auto-format as MM-DD-YYYY
    value = value.replace(/\D/g, '');
    if (value.length > 2) {
      value = value.slice(0, 2) + '-' + value.slice(2);
    }
    if (value.length > 5) {
      value = value.slice(0, 5) + '-' + value.slice(5, 9);
    }
    setFormData({...formData, dob: value});
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 p-4">
      <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
        <h1 className="text-2xl font-bold mb-6 text-center">Subscribe to Our Service</h1>
        <form onSubmit={handleSubmit} className="space-y-4">
          {/* Full Name */}
          <div>
            <label className="block text-sm font-medium mb-1">Full Name</label>
            <Input
              type="text"
              value={formData.fullName}
              onChange={(e) => setFormData({...formData, fullName: e.target.value})}
              placeholder="John Doe"
              required
            />
            {errors.fullName && <p className="text-red-500 text-xs mt-1">{errors.fullName}</p>}
          </div>

          {/* Date of Birth */}
          <div>
            <label className="block text-sm font-medium mb-1">Date of Birth (MM-DD-YYYY)</label>
            <Input
              type="text"
              value={formData.dob}
              onChange={handleDOBChange}
              placeholder="MM-DD-YYYY"
              required
            />
            {errors.dob && <p className="text-red-500 text-xs mt-1">{errors.dob}</p>}
          </div>

          {/* Phone Number */}
          <div>
            <label className="block text-sm font-medium mb-1">Phone Number (+1XXXXXXXXXX)</label>
            <Input
              type="tel"
              value={formData.phone}
              onChange={handlePhoneChange}
              placeholder="+11234567890"
              required
            />
            {errors.phone && <p className="text-red-500 text-xs mt-1">{errors.phone}</p>}
          </div>

          {/* Email */}
          <div>
            <label className="block text-sm font-medium mb-1">Email</label>
            <Input
              type="email"
              value={formData.email}
              onChange={(e) => setFormData({...formData, email: e.target.value})}
              placeholder="your@email.com"
              required
            />
            {errors.email && <p className="text-red-500 text-xs mt-1">{errors.email}</p>}
          </div>

          {/* Password */}
          <div>
            <label className="block text-sm font-medium mb-1">Password</label>
            <Input
              type="password"
              value={formData.password}
              onChange={(e) => setFormData({...formData, password: e.target.value})}
              placeholder="At least 8 chars (1 upper, 1 special, 1 number)"
              required
            />
            {errors.password && <p className="text-red-500 text-xs mt-1">{errors.password}</p>}
          </div>

          <Button type="submit" className="w-full">
            Subscribe
          </Button>
        </form>
      </div>
    </div>
  );
};

export default Subscribe;