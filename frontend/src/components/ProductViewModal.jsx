import {
  Dialog,
  DialogBackdrop,
  DialogPanel,
  DialogTitle,
} from "@headlessui/react";
import { Divider } from "@mui/material";
import { MdClose, MdDone } from "react-icons/md";
import Status from "./Status";

const ProductViewModal = ({
  open,
  setOpen,
  product,
  isAvailable,
}) => {
  if (!product) return null;

  const {
    productName,
    image,
    description,
    quantity,
    price,
    specialPrice,
  } = product;

  const handleClickClose = () => {
    setOpen(false);
  };

  return (
    <Dialog
      open={open}
      as="div"
      className="relative z-50"
      onClose={handleClickClose}
    >
      {/* Overlay */}
      <DialogBackdrop className="fixed inset-0 bg-black/50 backdrop-blur-sm transition-opacity" />

      <div className="fixed inset-0 overflow-y-auto">
        <div className="flex min-h-full items-center justify-center p-4">
          <DialogPanel
            className="
              relative w-full max-w-3xl overflow-hidden
              rounded-3xl bg-white shadow-2xl
              transition-all
            "
          >
            {/* Close button */}
            <button
              onClick={handleClickClose}
              className="
                absolute right-4 top-4 z-10
                rounded-full bg-white/80 p-2
                text-gray-700 shadow-md
                hover:bg-gray-100 transition
              "
            >
              <MdClose size={22} />
            </button>

            <div className="grid md:grid-cols-2">
              {/* LEFT - IMAGE */}
              <div className="bg-gray-100 flex items-center justify-center p-6">
                {image && (
                  <img
                    src={image}
                    alt={productName}
                    className="
                      h-[320px] w-full object-contain
                      transition duration-300 hover:scale-105
                    "
                  />
                )}
              </div>

              {/* RIGHT - CONTENT */}
              <div className="flex flex-col justify-between p-8">
                <div>
                  <DialogTitle
                    as="h3"
                    className="
                      text-2xl md:text-3xl
                      font-bold text-slate-800
                      mb-4
                    "
                  >
                    {productName}
                  </DialogTitle>

                  {/* Price + Status */}
                  <div className="flex items-center justify-between mb-5">
                    {specialPrice ? (
                      <div className="flex items-center gap-3">
                        <span className="text-gray-400 line-through text-lg">
                          ${Number(price).toFixed(2)}
                        </span>

                        <span className="text-3xl font-bold text-slate-900">
                          ${Number(specialPrice).toFixed(2)}
                        </span>
                      </div>
                    ) : (
                      <span className="text-3xl font-bold text-slate-900">
                        ${Number(price).toFixed(2)}
                      </span>
                    )}

                    {isAvailable ? (
                      <Status
                        text="In Stock"
                        icon={MdDone}
                        bg="bg-emerald-100"
                        color="text-emerald-700"
                      />
                    ) : (
                      <Status
                        text="Out of Stock"
                        icon={MdClose}
                        bg="bg-rose-100"
                        color="text-rose-700"
                      />
                    )}
                  </div>

                  <Divider />

                  {/* Description */}
                  <div className="mt-5 space-y-2">
                    <h4 className="font-semibold text-slate-800">
                      Description
                    </h4>

                    <p className="text-gray-600 leading-7">
                      {description}
                    </p>
                  </div>

                  {/* Quantity */}
                  <div className="mt-6">
                    <span className="text-sm text-gray-500">
                      Quantity:
                    </span>

                    <p className="font-semibold text-slate-800">
                      {quantity}
                    </p>
                  </div>
                </div>

                {/* Footer */}
                <div className="mt-8 flex justify-end">
                  <button
                    onClick={handleClickClose}
                    className="
                      rounded-xl bg-slate-900
                      px-6 py-3 text-sm font-semibold
                      text-white shadow-md
                      transition hover:scale-105
                      hover:bg-slate-800
                    "
                  >
                    Close
                  </button>
                </div>
              </div>
            </div>
          </DialogPanel>
        </div>
      </div>
    </Dialog>
  );
};

export default ProductViewModal;